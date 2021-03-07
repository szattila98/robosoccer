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
    private Double forceOfKick;

    public Ball() {
        super(new Position(70, 50)); // center of the soccer pitch
        this.playerId = null;
        this.forceOfKick = null;
    }

    @Override
    public void plotPositionsToMoveTo(Position start, Position end) {
        end = positionByKickForce(start, end);
        super.plotPositionsToMoveTo(start, end);
    }

    private Position positionByKickForce(Position start, Position end) {
        double newX = end.getX() + round((end.getX() - start.getX())*forceOfKick, 1);
        double newY = end.getY() + round((end.getY() - start.getY())*forceOfKick, 1);
        return new Position(newX, newY);
    }

    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
