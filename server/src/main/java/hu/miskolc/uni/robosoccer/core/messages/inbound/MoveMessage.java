package hu.miskolc.uni.robosoccer.core.messages.inbound;

import hu.miskolc.uni.robosoccer.core.Position;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Inbound message class for movement command.
 *
 * @author Attila Szőke
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MoveMessage {

    private final int playerId;
    private final Position destination;
}

