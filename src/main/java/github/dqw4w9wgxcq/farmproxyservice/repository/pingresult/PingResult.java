package github.dqw4w9wgxcq.farmproxyservice.repository.pingresult;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.Geo;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class PingResult {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Instant createdDate;
    private boolean success;
    @Nullable
    private Long latency;
    @ManyToOne
    private Ip ip;
    @ManyToOne
    private Geo geo;

    public PingResult(boolean success, Long latency, Ip ip, Geo geo) {
        this(null, null, success, latency, ip, geo);
    }
}
