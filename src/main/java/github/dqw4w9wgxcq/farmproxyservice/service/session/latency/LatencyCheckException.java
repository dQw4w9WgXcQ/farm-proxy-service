package github.dqw4w9wgxcq.farmproxyservice.service.session.latency;

public class LatencyCheckException extends Exception {
    public LatencyCheckException(String message) {
        super(message);
    }

    public LatencyCheckException(Throwable cause) {
        super(cause);
    }
}
