package github.dqw4w9wgxcq.farmproxyservice.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class Geo {
    @Id
    private String id;
    @CreationTimestamp
    private Instant createdDate;

    public Geo(String id) {
        this(id, null);
    }
}
