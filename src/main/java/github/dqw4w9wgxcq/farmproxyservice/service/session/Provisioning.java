package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.Session;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import github.dqw4w9wgxcq.farmproxyservice.service.geoban.GeoBanService;
import github.dqw4w9wgxcq.farmproxyservice.service.ipassociation.IpAssociationService;
import github.dqw4w9wgxcq.farmproxyservice.service.ipban.IpBanService;
import github.dqw4w9wgxcq.farmproxyservice.service.stabilitycheck.StabilityCheckService;
import github.dqw4w9wgxcq.farmproxyservice.service.stabilitycheck.StabilityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class Provisioning {
    private final SessionIds sessionIds;
    private final AwsCheckIpService awsCheckIpService;
    private final Proxies proxies;
    private final StabilityCheckService stabilityCheckService;
    private final IpAssociationService ipAssociationService;
    private final SessionPool sessionPool;
    private final GeoBanService geoBanService;
    private final IpBanService ipBanService;
    private final IpRepository ipRepository;

    @Async
    public void provisionAsync(String account, String geo) {
        tryProvision(account, geo);
    }

    public void tryProvision(String account, String geo) {
        try {
            for (int i = 0; i < 20; i++) {
                var sessionId = sessionIds.generate();

                var proxy = proxies.create(sessionId, geo);

                String ip;
                try {
                    var awsCheckIpResult = awsCheckIpService.ping(proxy);
                    if (awsCheckIpResult.latency() > StabilityCheckService.LATENCY_LIMIT) {
                        log.debug("initial ping latency {}", awsCheckIpResult.latency());
                        continue;
                    }
                    ip = awsCheckIpResult.ip();
                } catch (IOException e) {
                    log.info("ioe getting ip {}", e.toString());
                    continue;
                }

                ipRepository.save(Ip.create(ip));

                var accOnIp = sessionPool.getAccountForIp(ip);
                if (accOnIp != null) {
                    log.debug("{} already assigned to account {}", ip, accOnIp);
                    continue;
                }

                if (ipBanService.isBanned(ip)) {
                    log.debug("{} is banned", ip);
                    continue;
                }

                long latency;
                try {
                    latency = stabilityCheckService.assessLatency(proxy, ip, geo);
                } catch (StabilityException e) {
                    log.debug("stability check failed {}", e.toString());
                    continue;
                }

                var session = new Session(sessionId, geo, ip, latency);

                var associatedAccount = ipAssociationService.getAssociatedAccount(ip);
                if (associatedAccount != null) {
                    log.debug("ip {} is already assoicated with {}", ip, associatedAccount);
                    sessionPool.addSession(associatedAccount, session);
                    continue;
                }

                synchronized (sessionPool) {
                    var accOnIpAfter = sessionPool.getAccountForIp(ip);
                    if (accOnIpAfter != null) {
                        log.debug("after latency check, {} already assigned to account {}", ip, accOnIpAfter);
                        continue;
                    }
                    log.debug("adding session {} to account {}", session, account);
                    sessionPool.addSession(account, session);
                }
                return;
            }

            log.debug("couldnt find a good session after 20 tries, banning geo {}", geo);
            geoBanService.ban(geo);
            sessionPool.removePending(account);
        } catch (Exception e) {
            log.warn("unknown exception while provisioning", e);
            sessionPool.removePending(account);
        }
    }
}
