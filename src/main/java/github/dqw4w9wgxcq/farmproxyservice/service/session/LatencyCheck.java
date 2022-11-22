package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpResult;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatencyCheck {
    public static final int MAX_LATENCY = 4000;
    public static final int PINGS = 10;

    private final AwsCheckIpService awsCheckIpService;
    private final ProxyFactory proxyFactory;

    @SneakyThrows
    public long checkLatency(String sessionId, String geo, String ip) throws LatencyCheckException {
        var proxy = proxyFactory.create(sessionId, geo);

        long totalLatency = 0;
        for (int i = 0; i < PINGS; i++) {
            AwsCheckIpResult result;
            try {
                result = awsCheckIpService.ping(proxy);
            } catch (IOException e) {
                throw new LatencyCheckException(e);
            }

            if (!ip.equals(result.ip())) {
                throw new LatencyCheckException("ip changed old:" + ip + " new:" + result.ip());
            }

            if (result.latency() > MAX_LATENCY) {
                throw new LatencyCheckException("ping took too long: " + result.latency() + "ms");
            }

            totalLatency += result.latency();

            Thread.sleep(5000);
        }

        return totalLatency / PINGS;
    }
}
