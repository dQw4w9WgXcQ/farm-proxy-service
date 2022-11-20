package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoBanlist {
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
