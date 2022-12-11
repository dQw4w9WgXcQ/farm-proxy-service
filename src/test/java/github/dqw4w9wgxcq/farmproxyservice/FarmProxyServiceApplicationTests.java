package github.dqw4w9wgxcq.farmproxyservice;

import github.dqw4w9wgxcq.farmproxyservice.service.awscheckip.AwsCheckIpService;
import github.dqw4w9wgxcq.farmproxyservice.service.session.Proxies;
import github.dqw4w9wgxcq.farmproxyservice.service.session.SessionIds;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FarmProxyServiceApplicationTests {
	@Autowired
	private SessionIds sessionIds;
	@Autowired
	private Proxies proxies;
	@Autowired
	private AwsCheckIpService awsCheckIpService;

	@Test
	void contextLoads() {
	}
}
