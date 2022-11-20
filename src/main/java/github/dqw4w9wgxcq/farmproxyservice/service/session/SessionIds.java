package github.dqw4w9wgxcq.farmproxyservice.service.session;

import org.springframework.stereotype.Component;

@Component
public class SessionIds {
    private volatile int counter = 0;

    public synchronized String generate() {
        return Integer.toString(counter++);
    }
}
