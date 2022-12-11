package github.dqw4w9wgxcq.farmproxyservice.repository.geoban;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.GeoBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoBanRepository extends JpaRepository<GeoBan, Long> {
}
