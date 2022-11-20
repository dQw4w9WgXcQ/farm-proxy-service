package github.dqw4w9wgxcq.farmproxyservice.config;

import github.dqw4w9wgxcq.farmproxyservice.config.properties.ProxyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
@EnableAsync
public class Config {
}
