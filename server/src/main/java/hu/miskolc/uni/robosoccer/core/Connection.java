package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Connection {

    private String sessionId;
    private Date date;
    private ConnectionType connectionType;
}
