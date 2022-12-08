package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.Session;
import github.dqw4w9wgxcq.farmproxyservice.service.geobanlist.GeoBanlistService;
import github.dqw4w9wgxcq.farmproxyservice.service.ipassociation.IpAssociationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {
    private final SessionPool sessionPool;
    private final Provisioning provisioning;
    private final GeoBanlistService geoBanlistService;
    private final IpAssociationService ipAssociationService;
    private final GeoRepository geoRepository;

    @Nullable
    public Session getSessionElseProvision(String account, String geo) {
        Session session;
        synchronized (sessionPool) {
            session = sessionPool.getSession(account);
            if (session == null) {
                sessionPool.setPending(account);
                provisioning.provisionAsync(account, geo);
                return null;
            }
        }

        ipAssociationService.associateIpWithAccount(account, session.ip());
        return session;
    }

    //for debugging
    public Map<String, List<Session>> getAllSessions() {
        return sessionPool.all();
    }

    public Session refreshSession(String account, String geo, String sessionId) {
        synchronized (sessionPool) {
            sessionPool.removeSession(account, sessionId);

            //there may already be another session for the account in queue
            return getSessionElseProvision(account, geo);
        }
    }

    public void freeSession(String account, String session) {
        synchronized (sessionPool){
            sessionPool.removeSession(account,session);
        }
    }
}
