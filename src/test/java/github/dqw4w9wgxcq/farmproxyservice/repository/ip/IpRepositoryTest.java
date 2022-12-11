package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import github.dqw4w9wgxcq.farmproxyservice.repository.Ip;
import github.dqw4w9wgxcq.farmproxyservice.repository.IpRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class IpRepositoryTest {
    @Autowired
    private IpRepository ipRepository;

    @Test
    void testCountBySubnet() {
        ipRepository.save(Ip.create("1.2.3.4"));
        ipRepository.save(Ip.create("1.2.3.5"));
        ipRepository.save(Ip.create("1.2.3.6"));
        ipRepository.save(Ip.create("1.3.3.5"));

        assert ipRepository.countByA(1) == 4;
        assert ipRepository.countByAAndB(1, 2) == 3;
        assert ipRepository.countByAAndBAndC(1, 2, 3) == 3;
    }
}