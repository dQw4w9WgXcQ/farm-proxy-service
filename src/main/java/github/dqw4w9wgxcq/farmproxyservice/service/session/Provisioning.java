package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import github.dqw4w9wgxcq.farmproxyservice.service.model.Session;
import github.dqw4w9wgxcq.farmproxyservice.service.session.latency.LatencyCheck;
import github.dqw4w9wgxcq.farmproxyservice.service.session.latency.LatencyCheckException;
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
    private final ProxyFactory proxyFactory;
    private final LatencyCheck latencyCheck;
    private final IpAssociation ipAssociation;
    private final SessionPool sessionPool;
    private final GeoBanlist geoBanlist;
    private final IpBanlist ipBanlist;

    @Async
    public void provisionAsync(String account, String geo) {
        for (int i = 0; i < 20; i++) {
            var sessionId = sessionIds.generate();

            String ip;
            try {
                var awsCheckIpResult = awsCheckIpService.ping(proxyFactory.create(sessionId, geo));
                System.out.println("kanker");
                System.out.println(awsCheckIpResult);
                if (awsCheckIpResult.latency() > LatencyCheck.MAX_LATENCY) {
                    log.debug("initial ping latency {}", awsCheckIpResult.latency());
                    continue;
                }
                ip = awsCheckIpResult.ip();
            } catch (IOException e) {
                System.out.println("ioe getting ip");
                log.info("ioe getting ip", e);
                continue;
            }

            var accOnIp = sessionPool.getAccountForIp(ip);
            if (accOnIp != null) {
                log.debug("{} already assigned to account {}", ip, accOnIp);
                continue;
            }

            if (ipBanlist.isBanned(ip)) {
                log.debug("{} is banned", ip);
                continue;
            }

            long latency;
            try {
                latency = latencyCheck.checkLatency(sessionId, geo, ip);
            } catch (LatencyCheckException e) {
                log.debug("stability check failed", e);
                continue;
            }

            var session = new Session(sessionId, geo, ip, latency);

            var associatedAccount = ipAssociation.getAssociatedAccount(ip);
            if (associatedAccount != null) {
                log.debug("ip {} is already assoicated with {}", ip, associatedAccount);
                sessionPool.addSession(associatedAccount, session);
                continue;
            }

            synchronized (sessionPool) {
                var accOnIp2 = sessionPool.getAccountForIp(ip);
                if (accOnIp2 != null) {
                    log.debug("after stability check, {} already assigned to account {}", ip, accOnIp2);
                    continue;
                }
                sessionPool.addSession(account, session);
            }
            return;
        }

        log.debug("couldnt find a good session after 20 tries, banning geo {}", geo);
        geoBanlist.ban(geo);
    }
}
