package github.dqw4w9wgxcq.farmproxyservice.service.geoban;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.geoban.GeoBan;
import github.dqw4w9wgxcq.farmproxyservice.domain.GeoBanReason;
import github.dqw4w9wgxcq.farmproxyservice.repository.geoban.GeoBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoBanService {
    public static final int BAN_DURATION = 7 * 24 * 60_000;

    private final GeoBanRepository geoBanRepository;
    private final GeoRepository geoRepository;

    public boolean isBanned(String geo) {
        return false;//todo
    }

    public void ban(String geo, GeoBanReason reason) {
        var geoEntity = geoRepository.getReferenceById(geo);
        geoBanRepository.save(new GeoBan(geoEntity, reason));
    }
}
