package hu.miskolc.uni.robosoccer.core.messages.inbound;

import hu.miskolc.uni.robosoccer.core.Position;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class KickMessage {

    private final Position destination;
    private final double forceOfKick;

}
