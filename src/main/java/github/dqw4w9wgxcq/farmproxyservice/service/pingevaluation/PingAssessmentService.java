package github.dqw4w9wgxcq.farmproxyservice.service.pingevaluation;

import github.dqw4w9wgxcq.farmproxyservice.repository.Geo;
import github.dqw4w9wgxcq.farmproxyservice.repository.GeoRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.PingAssessment;
import github.dqw4w9wgxcq.farmproxyservice.repository.PingAssessmentRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.Proxy;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpResult;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PingAssessmentService {
    public static final int DELAY = 5000;
    public static final int LATENCY_LIMIT = 4000;
    public static final int PINGS = 10;

    private final AwsCheckIpService awsCheckIpService;
    private final PingAssessmentRepository pingAssessmentRepository;
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

        geoRepository.save(new Geo(geo));

        var pingResult = new PingAssessment(latency, ipRepository.getReferenceById(ip), geoRepository.getReferenceById(geo));
        pingAssessmentRepository.save(pingResult);

        return latency;
    }
}
