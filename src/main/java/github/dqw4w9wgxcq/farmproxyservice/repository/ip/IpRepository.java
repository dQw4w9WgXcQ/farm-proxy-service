package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends JpaRepository<Ip, String> {
    long countBySubnetA(int subnetA);

    long countBySubnetAAndSubnetB(int subnetA, int subnetB);

    long countBySubnetAAndSubnetBAndSubnetC(int subnetA, int subnetB, int subnetC);
}
