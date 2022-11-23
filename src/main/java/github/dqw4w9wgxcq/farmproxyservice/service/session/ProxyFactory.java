package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.config.properties.ProxyProperties;
import github.dqw4w9wgxcq.farmproxyservice.service.Proxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProxyFactory {
    private final ProxyProperties proxyProperties;

    public Proxy create(String session, String geo) {
        log.debug("create with id:{} geo:{}", session, geo);

        var split = geo.split("_");
        if (split.length > 2) {
            throw new IllegalArgumentException("weird geo:" + geo);
        }

        String country = split[0];

        String state = null;
        if (split.length == 2) {
            state = split[1];
        }

        var sb = new StringBuilder()
                .append(proxyProperties.password());

        if (country != null) {
            sb.append("_country-").append(country);
        }

        //non us geo has no state
        if (state != null) {
            sb.append("_state-").append(state);
        }

        sb.append("_session-").append(session).append("_lifetime-24h");

        var password = sb.toString();

        log.debug("password:" + password);
        return new Proxy(proxyProperties.address(), proxyProperties.port(), proxyProperties.username(), password);
    }
}
