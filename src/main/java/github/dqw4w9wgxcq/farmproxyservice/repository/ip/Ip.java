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
    private int subnetA;
    private int subnetB;
    private int subnetC;
    private int subnetD;

    public static Ip create(String ip) {
        var ints = toInts(ip);
        return new Ip()
                .id(ip)
                .subnetA(ints[0])
                .subnetB(ints[1])
                .subnetC(ints[2])
                .subnetD(ints[3]);
    }

    private static int[] toInts(String ipv4) {
        var split = ipv4.split("\\.");

        Preconditions.checkArgument(split.length == 4);

        return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
    }
}
