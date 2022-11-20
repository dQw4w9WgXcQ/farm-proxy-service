package github.dqw4w9wgxcq.farmproxyservice.service.session;

import github.dqw4w9wgxcq.farmproxyservice.repository.ip.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpAssociation {
    private final IpRepository ipRepository;

    @Nullable
    public String getAssociatedAccount(String ip) {
        return null;//todo
    }

    public void associateAccountWithIp(String account, String ip) {
//todo
    }
}
