package github.dqw4w9wgxcq.farmproxyservice.repository.pingresult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PingResultRepository extends JpaRepository<PingResult, Long> {
}
