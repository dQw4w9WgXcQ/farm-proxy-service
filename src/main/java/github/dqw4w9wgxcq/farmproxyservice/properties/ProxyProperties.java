package github.dqw4w9wgxcq.farmproxyservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "proxy")
@ConstructorBinding
public record ProxyProperties(String address, Integer httpPort, Integer socksPort, String username, String password) {
}
