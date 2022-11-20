package github.dqw4w9wgxcq.farmproxyservice.service.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Proxy(String address, int port, String username, String password) {
    @Override
    public String toString() {
        return password;
    }
}