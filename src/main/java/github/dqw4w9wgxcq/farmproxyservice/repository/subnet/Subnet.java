package github.dqw4w9wgxcq.farmproxyservice.repository.subnet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class Subnet {
    @Id
    private String id;
}
