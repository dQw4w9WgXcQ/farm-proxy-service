package github.dqw4w9wgxcq.farmproxyservice.service.ipbanlist;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpBanlistService {
    private final IpRepository ipRepository;

    @Autowired
    public IpBanlistService(IpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public void ban(String geo) {
//todo
    }

    public boolean isBanned(String ip) {
        var ipEntity = ipRepository.findById(ip).orElse(null);
        if (ipEntity == null) {
            return false;
        }

        return false;//todo
    }
}
