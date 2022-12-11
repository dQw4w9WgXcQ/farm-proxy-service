package github.dqw4w9wgxcq.farmproxyservice.service.ipban;

import github.dqw4w9wgxcq.farmproxyservice.repository.IpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IpBanService {
    private final IpRepository ipRepository;

    @Autowired
    public IpBanService(IpRepository ipRepository) {
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
