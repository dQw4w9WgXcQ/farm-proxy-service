package github.dqw4w9wgxcq.farmproxyservice;

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

    @GetMapping("ping")
    public String ping(@RequestParam(required = false) String session, @RequestParam(required = false) String geo) {
        try {
            if (session == null) {
                session = Long.toString(System.currentTimeMillis());
            }

            var proxyTemplate = proxyFactory.create(session, geo);
            log.info(proxyTemplate.toString());
            var result = awsCheckIp.ping(proxyTemplate);
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
}
