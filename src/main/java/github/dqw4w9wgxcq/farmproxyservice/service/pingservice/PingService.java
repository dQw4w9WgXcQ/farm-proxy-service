package github.dqw4w9wgxcq.farmproxyservice.service.pingservice;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.pingresult.PingResult;
import github.dqw4w9wgxcq.farmproxyservice.repository.pingresult.PingResultRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.Proxy;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpResult;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PingService {
    public static final int DELAY = 5000;
    public static final int LATENCY_LIMIT = 4000;
    public static final int PINGS = 10;

    private final AwsCheckIpService awsCheckIpService;
    private final PingResultRepository pingResultRepository;
    private final IpRepository ipRepository;
    private final GeoRepository geoRepository;

    public long testLatency(Proxy proxy, String ip, String geo) throws PingException {
        long totalLatency = 0;
        for (int i = 0; i < PINGS; i++) {
            AwsCheckIpResult result;
            try {
                result = awsCheckIpService.ping(proxy);
            } catch (IOException e) {
                throw new PingException(e);
            }

            if (!ip.equals(result.ip())) {
                throw new PingException("ip changed old:" + ip + " new:" + result.ip());
            }

            if (result.latency() > LATENCY_LIMIT) {
                throw new PingException("ping took too long: " + result.latency() + "ms");
            }

            totalLatency += result.latency();

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                log.warn("interrupted during delay", e);
            }
        }

        var latency = totalLatency / PINGS;

        var pingResult = new PingResult(latency, ipRepository.getReferenceById(ip), geoRepository.getReferenceById(geo));
        pingResultRepository.save(pingResult);

        return latency;
    }
}
