package github.dqw4w9wgxcq.farmproxyservice;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import github.dqw4w9wgxcq.farmproxyservice.service.session.ProxyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.net.SocketTimeoutException;

@Slf4j
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class FarmProxyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarmProxyServiceApplication.class, args);
    }

    private final AwsCheckIpService awsCheckIp;
    private final ProxyFactory proxyFactory;
    private final IpRepository ipRepository;

    @GetMapping("ping")
    public String ping(@RequestParam(required = false) String session, @RequestParam String geo) {
        try {
            if (session == null) {
                session = Long.toString(System.currentTimeMillis());
            }

            var proxy = proxyFactory.create(session, geo);
            log.info(proxy.toString());
            var result = awsCheckIp.ping(proxy);
            return result.ip() + " <br>" +
                    result.latency() + "ms<br>" +
                    "<a href=\"https://db-ip.com/" + result.ip() + "\">db-ip</a><br>" +
                    "<a href=\"https://www.ipqualityscore.com/free-ip-lookup-proxy-vpn-test/lookup/" + result.ip() + "\">ipqualityscore</a>";
        } catch (SocketTimeoutException e) {
            return "checkip.amazonaws.com ping timed out";
        } catch (IOException e) {
            throw new ServerErrorException("checkip.amazonaws.com ping failed", e);
        }
    }

    @GetMapping("test")
    public void test() {
        ipRepository.save(Ip.create("1.2.3.4"));
        ipRepository.save(Ip.create("1.2.3.5"));
        ipRepository.save(Ip.create("1.2.3.6"));
        ipRepository.save(Ip.create("1.3.3.5"));

        System.out.println(ipRepository.countBySubnetA(1));
        System.out.println(ipRepository.countBySubnetAAndSubnetBAndSubnetC(1, 2, 3));
    }
}
