package github.dqw4w9wgxcq.farmproxyservice.repository.ipban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpBanRepository extends JpaRepository<IpBan, String> {
}
