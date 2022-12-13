package github.dqw4w9wgxcq.farmproxyservice.repository.stabilitycheckresult;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.Geo;
import github.dqw4w9wgxcq.farmproxyservice.repository.ip.Ip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.Nullable;

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
public class StabilityCheckResult {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Instant date;
    private Boolean success;
    @Nullable
    private Integer averageLatency;
    @ManyToOne
    private Ip ip;
    @ManyToOne
    private Geo geo;

    public StabilityCheckResult(Boolean success, @Nullable Integer averageLatency, Ip ip, Geo geo) {
        this(null, null, success, averageLatency, ip, geo);
    }
}
