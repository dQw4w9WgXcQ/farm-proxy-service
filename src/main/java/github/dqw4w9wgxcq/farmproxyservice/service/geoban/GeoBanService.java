package github.dqw4w9wgxcq.farmproxyservice.service.geoban;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoBanService {
    public static final int BAN_DURATION = 7 * 24 * 60_000;

    public boolean isBanned(String geo) {
        return false;//todo
    }

    public void ban(String geo) {

    }
}
