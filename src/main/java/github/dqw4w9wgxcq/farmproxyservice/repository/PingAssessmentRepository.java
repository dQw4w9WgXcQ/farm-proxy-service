package github.dqw4w9wgxcq.farmproxyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PingAssessmentRepository extends JpaRepository<PingAssessment, Long> {
}
