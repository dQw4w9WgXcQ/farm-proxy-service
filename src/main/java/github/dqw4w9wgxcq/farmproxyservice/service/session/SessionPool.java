package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.service.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
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
            log.debug("{} already pending", account);
            return;
        }

        pendingAccounts.add(account);
    }

    public synchronized Map<String, List<Session>> all() {
        var all = new HashMap<String, List<Session>>();
        //deep clone
        for (var entry : activeSessions.entrySet()) {
            var account = entry.getKey();
            var sessions = entry.getValue();

            all.put(account, new ArrayList<>(sessions));
        }

        return all;
    }

    public synchronized void removePending(String account) {
        pendingAccounts.remove(account);
    }
}
