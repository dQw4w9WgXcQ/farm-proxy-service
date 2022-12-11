package github.dqw4w9wgxcq.farmproxyservice.repository.geoban;

import github.dqw4w9wgxcq.farmproxyservice.repository.geo.Geo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

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
public class GeoBan {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Instant date;
    @ManyToOne
    private Geo geo;
    private Reason reason;

    public enum Reason {
        TOO_MANY_DUPLICATE_IPS
    }
}
