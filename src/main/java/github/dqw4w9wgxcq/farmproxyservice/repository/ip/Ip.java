package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Ip {
    @Id
    private String id;
}
