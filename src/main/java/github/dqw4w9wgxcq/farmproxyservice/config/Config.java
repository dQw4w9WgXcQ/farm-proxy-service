package github.dqw4w9wgxcq.farmproxyservice.config;

import github.dqw4w9wgxcq.farmproxyservice.config.properties.ProxyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
@EnableAsync
public class Config implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        return Executors.newCachedThreadPool();
    }
}
