package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends JpaRepository<Ip, String> {
    long countByA(int A);

    long countByAAndB(int A, int B);

    long countByAAndBAndC(int A, int B, int C);
}
