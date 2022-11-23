package github.dqw4w9wgxcq.farmproxyservice.service.geobanlist;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoBanlistService {
    public static final int BAN_DURATION = 7 * 24 * 60_000;

    private final GeoRepository geoRepository;

    public boolean isBanned(String geo) {
        var geoEntity = geoRepository.findById(geo).orElse(null);

        if (geoEntity == null) {
            return false;
        }

        return false;//todo
    }

    public void ban(String geo) {
//todo
    }
}
