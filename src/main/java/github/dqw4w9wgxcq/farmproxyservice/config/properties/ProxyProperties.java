package github.dqw4w9wgxcq.farmproxyservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "proxy")
@ConstructorBinding
public record ProxyProperties(String address, Integer port, String username, String password) {
}
