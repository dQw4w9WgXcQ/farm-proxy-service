package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.service.model.Session;
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
    private final GeoBanlist geoBanlist;

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

        return session;
    }

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
}
