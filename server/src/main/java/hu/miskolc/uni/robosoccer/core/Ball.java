package hu.miskolc.uni.robosoccer.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode(callSuper = false)
public class Ball extends Movable {

    private Integer playerId; // null if no one has it
    @JsonIgnore
    private Integer forceOfKick;

    public Ball() {
        super(new Position(70, -50)); // center of the soccer pitch
        this.playerId = null;
        this.forceOfKick = null;
    }

    @Override
    public void plotPositionsToMoveTo(Position start, Position end) {
        // TODO implement with force of kick
    }
}
