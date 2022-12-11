package github.dqw4w9wgxcq.farmproxyservice.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Proxy(String address, int httpPort, int socksPort, String username, String password) {
    @Override
    public String toString() {
        return password;
    }
}