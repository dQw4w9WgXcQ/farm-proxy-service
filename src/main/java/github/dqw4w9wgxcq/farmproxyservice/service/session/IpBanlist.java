package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import github.dqw4w9wgxcq.farmproxyservice.repository.subnet.SubnetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpBanlist {
    private final IpRepository ipRepository;
    private final SubnetRepository subnetRepository;

    @Autowired
    public IpBanlist(IpRepository ipRepository, SubnetRepository subnetRepository) {
        this.ipRepository = ipRepository;
        this.subnetRepository = subnetRepository;
    }

    public boolean isBanned(String ip) {
        var ipEntity = ipRepository.findById(ip).orElse(null);
        if (ipEntity == null) {
            return false;
        }

        return false;//todo
    }

    public void ban(String geo) {
//todo
    }
}
