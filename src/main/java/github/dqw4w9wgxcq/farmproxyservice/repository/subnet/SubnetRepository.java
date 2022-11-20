package github.dqw4w9wgxcq.farmproxyservice.repository.subnet;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubnetRepository extends JpaRepository<Ip, String> {
}
