package github.dqw4w9wgxcq.farmproxyservice.service.stabilitycheck;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.stabilitycheckresult.StabilityCheckResult;
import github.dqw4w9wgxcq.farmproxyservice.repository.stabilitycheckresult.StabilityCheckResultRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.Proxy;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpResult;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StabilityCheckService {
    public static final int DELAY = 5000;
    public static final int LATENCY_LIMIT = 4000;
    public static final int PINGS = 10;

    private final AwsCheckIpService awsCheckIpService;
    private final StabilityCheckResultRepository stabilityCheckResultRepository;
    private final IpRepository ipRepository;
    private final GeoRepository geoRepository;

    //geo and ip must already be persisted
    public long assessLatency(Proxy proxy, String ip, String geo) throws StabilityException {
        int totalLatency = 0;
        for (int i = 1; i <= PINGS; i++) {
            AwsCheckIpResult result;
            try {
                result = awsCheckIpService.ping(proxy);
            } catch (IOException e) {
                saveResult(false, totalLatency, i, ip, geo);
                throw new StabilityException(e);
            }

            if (!ip.equals(result.ip())) {
                saveResult(false, totalLatency, i, ip, geo);
                throw new StabilityException("ip changed old:" + ip + " new:" + result.ip());
            }

            if (result.latency() > LATENCY_LIMIT) {
                saveResult(false, totalLatency, i, ip, geo);
                throw new StabilityException("ping took too long: " + result.latency() + "ms");
            }

            totalLatency += result.latency();

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                log.warn("interrupted during delay", e);
            }
        }

        saveResult(true, totalLatency, PINGS, ip, geo);

        return totalLatency / PINGS;
    }

    private void saveResult(boolean success, int totalLatency, int count, String ip, String geo) {
        var pingResult = new StabilityCheckResult(
                success,
                success ? totalLatency / count : null,
                ipRepository.getReferenceById(ip),
                geoRepository.getReferenceById(geo)
        );
        stabilityCheckResultRepository.save(pingResult);
    }
}
