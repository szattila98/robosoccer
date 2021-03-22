package hu.miskolc.uni.robosoccer.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a position on the soccer field.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Position {

    private double x;
    private double y;

    public void move(Position newPosition) {
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }

    public Position invert() {
        return new Position(Match.PITCH_WIDTH - this.x, Match.PITCH_HEIGHT - this.y);
    }

    public Position toNormalizedDirectionVector(Position nextPosition) {
        Position directionVector = new Position(nextPosition.x - this.x, nextPosition.y - this.y);
        double length = Math.sqrt(Math.pow(directionVector.x, 2) + Math.pow(directionVector.y, 2));
        return new Position(directionVector.x / length, directionVector.y / length);
    }

    public Position multiplyByScalar(double step) {
        return new Position(this.x * step, this.y * step);
    }

    public Position plus(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

}
