package github.dqw4w9wgxcq.farmproxyservice.service.awscheckip;

import github.dqw4w9wgxcq.farmproxyservice.service.Proxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsCheckIpService {
    public AwsCheckIpResult ping(Proxy proxy) throws IOException {
        var inetAddress = new InetSocketAddress(proxy.address(), proxy.port());
        var credential = Credentials.basic(proxy.username(), proxy.password());

        var client = new OkHttpClient.Builder()
                .proxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, inetAddress))
//                .proxyAuthenticator((route, response) -> response.request().newBuilder()
//                        .header("Proxy-Authorization", credential)
//                        .build())
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Proxy-Authorization", credential)
                            //strip default headers to save bandwidth through proxy
//                            .removeHeader("Accept-Encoding")
                            .removeHeader("User-Agent")
                            .removeHeader("Connection")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        var req = new Request.Builder()
                .url("http://checkip.amazonaws.com")
                .build();

        var call = client.newCall(req);

        try (var res = call.execute()) {
            if (res.code() != 200) {
                throw new IOException("checkip.amazonaws.com ping through proxy response code not 200, " + res.code());
            }

            var body = res.body();
            if (body == null) {
                throw new IOException("checkip.amazonaws.com response has null body");
            }

            var ip = body.string().trim();

            var latency = res.receivedResponseAtMillis() - res.sentRequestAtMillis();

            log.debug("ip:{} latency:{}", ip, latency);

            return new AwsCheckIpResult(ip, latency);
        }
    }
}
