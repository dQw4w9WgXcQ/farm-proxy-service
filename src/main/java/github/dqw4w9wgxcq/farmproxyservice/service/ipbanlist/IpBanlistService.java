package github.dqw4w9wgxcq.farmproxyservice.service.ipbanlist;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.subnet.SubnetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpBanlistService {
    private final IpRepository ipRepository;
    private final SubnetRepository subnetRepository;

    @Autowired
    public IpBanlistService(IpRepository ipRepository, SubnetRepository subnetRepository) {
        this.ipRepository = ipRepository;
        this.subnetRepository = subnetRepository;
    }

    public void ban(String geo) {
//todo
    }

    public boolean isBanned(String ip) {
        var ipEntity = ipRepository.findById(ip).orElse(null);
        if (ipEntity == null) {
            return false;
        }


    }
}
