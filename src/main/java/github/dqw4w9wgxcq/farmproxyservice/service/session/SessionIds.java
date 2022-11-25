package github.dqw4w9wgxcq.farmproxyservice.service.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SessionIds {
    private static final long ORIGIN = Long.parseLong("10000000", 36);
    private static final long BOUND = Long.parseLong("zzzzzzzz", 36) + 1;

    public String generate() {
        return Long.toString(ThreadLocalRandom.current().nextLong(ORIGIN, BOUND), 36);
    }
}
