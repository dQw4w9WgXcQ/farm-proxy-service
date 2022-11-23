package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpRepository extends JpaRepository<Ip, String> {
    long countBySubnetA(short subnetA);

    long countBySubnetAAndSubnetB(short subnetA, short subnetB);

    long countBySubnetAAndSubnetBAndSubnetC(short subnetA, short subnetB, short subnetC);
}
