package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.domain.GeoBanReason;
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
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class Provisioning {
    public static final int TRIES = 20;

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
        provision(account, geo);
    }

    public void provision(String account, String geo) {
        try {
            for (int i = 0; i < TRIES; i++) {
                var session = tryCreateSession(geo);
                if (session == null) {
                    continue;
                }

                synchronized (sessionPool) {
                    var accOnIpAfter = sessionPool.getAccountForIp(session.ip());
                    if (accOnIpAfter != null) {
                        log.debug("after latency check, {} already assigned to account {}", session.ip(), accOnIpAfter);
                        continue;
                    }

                    log.debug("adding session {} to account {}", session, account);
                    sessionPool.addSession(account, session);
                    return;
                }
            }

            log.debug("couldnt find a good session after 20 tries, banning geo {}", geo);
            geoBanService.ban(geo, GeoBanReason.STABILITY_CHECK);
            sessionPool.removePending(account);
        } catch (Exception e) {
            log.warn("unknown exception while provisioning", e);
            sessionPool.removePending(account);
        }
    }

    /**
     * @return if null, should retry
     */
    @Nullable
    private Session tryCreateSession(String geo) {
        var sessionId = sessionIds.generate();
        var proxy = proxies.create(sessionId, geo);

        String initialIp;
        try {
            var awsCheckIpResult = awsCheckIpService.ping(proxy);
            if (awsCheckIpResult.latency() > StabilityCheckService.LATENCY_LIMIT) {
                log.debug("initial ping latency {}", awsCheckIpResult.latency());
                return null;
            }
            initialIp = awsCheckIpResult.ip();
        } catch (IOException e) {
            log.info("ioe getting ip {}", e.toString());
            return null;
        }

        var accOnIp = sessionPool.getAccountForIp(initialIp);
        if (accOnIp != null) {
            log.debug("{} already assigned to account {}", initialIp, accOnIp);
            return null;
        }

        if (ipBanService.isBanned(initialIp)) {
            log.debug("{} is banned", initialIp);
            return null;
        }

        long latency;
        try {
            latency = stabilityCheckService.assessLatency(proxy, initialIp, geo);
        } catch (StabilityException e) {
            log.debug("stability issue", e);
            return null;
        }

        var session = new Session(sessionId, geo, initialIp, latency);

        var associatedAccount = ipAssociationService.getAssociatedAccount(initialIp);
        if (associatedAccount != null) {
            log.debug("ip {} is already assoicated with {}", initialIp, associatedAccount);
            sessionPool.addSession(associatedAccount, session);
            return null;
        }

        return session;
    }
}
