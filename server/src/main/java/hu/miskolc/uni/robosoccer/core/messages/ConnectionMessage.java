package hu.miskolc.uni.robosoccer.core.messages;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import lombok.*;

import java.util.Date;

/**
 * Message sent about the status of connections.
 *
 * @author Attila Szőke
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ConnectionMessage {

    private User user;
    private Date date;
    private ConnectionType connectionType;
}
