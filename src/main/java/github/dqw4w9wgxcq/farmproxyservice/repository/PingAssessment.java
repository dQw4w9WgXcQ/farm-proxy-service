package github.dqw4w9wgxcq.farmproxyservice.repository;

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
public class PingAssessment {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Instant date;
    @Nullable
    private Long averageLatency;
    @ManyToOne
    private Ip ip;
    @ManyToOne
    private Geo geo;

    public PingAssessment(Long averageLatency, Ip ip, Geo geo) {
        this(null, null, averageLatency, ip, geo);
    }
}
