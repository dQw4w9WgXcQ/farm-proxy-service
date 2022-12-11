package github.dqw4w9wgxcq.farmproxyservice.service.ipassociation;

import github.dqw4w9wgxcq.farmproxyservice.repository.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpAssociationService {
    private final IpRepository ipRepository;

    @Nullable
    public String getAssociatedAccount(String ip) {
        return null;//todo
    }

    public void associate(String account, String ip) {
//todo
    }
}
