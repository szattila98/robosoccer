package hu.miskolc.uni.robosoccer.core.messages.outbound;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * Outbound message which carries information about the status of connections.
 *
 * @author Attila Szőke
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserConnectionStateMessage {

    private final User user;
    private final Date date;
    private final ConnectionType connectionType;
}
