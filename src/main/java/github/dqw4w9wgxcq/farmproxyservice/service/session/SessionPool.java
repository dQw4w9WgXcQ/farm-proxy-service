package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.service.model.Session;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SessionPool {
    private final Map<String, List<Session>> activeSessions = new HashMap<>();
    private final Set<String> pendingAccounts = new HashSet<>();

    @Nullable
    public synchronized Session getSession(String account) {
        var sessionList = activeSessions.get(account);
        if (sessionList != null && !sessionList.isEmpty()) {
            return sessionList.get(0);
        } else {
            return null;
        }
    }

    @Nullable
    public synchronized String getAccountForIp(String ip) {
        for (var entry : activeSessions.entrySet()) {
            for (var session : entry.getValue()) {
                if (session.ip().equals(ip)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    public synchronized void addSession(String account, Session session) {
        pendingAccounts.remove(account);
        if (!activeSessions.containsKey(account)) {
            activeSessions.put(account, new ArrayList<>());
        }

        activeSessions.get(account).add(session);
    }

    public synchronized void removeSession(String account, String sessionId) {
        var sessions = activeSessions.get(account);
        if (sessions == null) {
            return;
        }

        sessions.removeIf(s -> s.id().equals(sessionId));
    }

    public synchronized void setPending(String account) {
        if (pendingAccounts.contains(account)) {
            throw new IllegalStateException("already pending:" + account);
        }

        pendingAccounts.add(account);
    }
}
