package github.dqw4w9wgxcq.farmproxyservice.service.stabilitycheck;

public class StabilityException extends Exception {
    public StabilityException(String message) {
        super(message);
    }

    public StabilityException(Throwable cause) {
        super(cause);
    }
}
