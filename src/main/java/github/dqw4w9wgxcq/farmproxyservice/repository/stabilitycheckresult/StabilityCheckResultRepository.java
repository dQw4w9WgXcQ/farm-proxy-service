package github.dqw4w9wgxcq.farmproxyservice.repository.stabilitycheckresult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StabilityCheckResultRepository extends JpaRepository<StabilityCheckResult, Long> {
}
