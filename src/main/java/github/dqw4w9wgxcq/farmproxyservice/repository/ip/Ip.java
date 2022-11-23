package github.dqw4w9wgxcq.farmproxyservice.repository.ip;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Arrays;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class Ip {
    @Id
    private String id;
    @CreationTimestamp
    private Instant createdDate;
    private short subnetA;
    private short subnetB;
    private short subnetC;
    private short subnetD;

    public static Ip create(String ip) {
        var ints = toInts(ip);
        return new Ip()
                .id(ip)
                .subnetA((short) ints[0])
                .subnetB((short) ints[1])
                .subnetC((short) ints[2])
                .subnetD((short) ints[3]);
    }

    public static int[] toInts(String ipv4) {
        var split = ipv4.split("\\.");

        Preconditions.checkArgument(split.length == 4);

        return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
    }
}
