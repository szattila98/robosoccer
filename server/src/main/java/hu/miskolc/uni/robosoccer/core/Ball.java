package hu.miskolc.uni.robosoccer.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the soccer ball.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Ball {

    private Integer playerId; // null if no one has it
    private Position position;

    public Ball() { // TODO better position when there is a field
        this.playerId = null;
        this.position = new Position(0, 0);
    }
}
