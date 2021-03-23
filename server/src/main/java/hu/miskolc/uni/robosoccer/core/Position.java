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

    /**
     * Changes the coordinates of the position.
     *
     * @param newPosition new position
     */
    public void move(Position newPosition) {
        this.x = newPosition.getX();
        this.y = newPosition.getY();
    }

    /**
     * Inverts a position.
     *
     * @return the inverted position
     */
    public Position invert() {
        return new Position(Match.PITCH_WIDTH - this.x, Match.PITCH_HEIGHT - this.y);
    }

    /**
     * Calculates a normalized direction vector between the caller position and another.
     *
     * @param nextPosition the other position
     * @return the normalized direction vector
     */
    public Position toNormalizedDirectionVector(Position nextPosition) {
        Position directionVector = new Position(nextPosition.x - this.x, nextPosition.y - this.y);
        double length = Math.sqrt(Math.pow(directionVector.x, 2) + Math.pow(directionVector.y, 2));
        return new Position(directionVector.x / length, directionVector.y / length);
    }

    /**
     * Multiplies a position by scalar. It is used on vectors to get the next position.
     *
     * @param scalar the scalar
     * @return multiplied vector
     */
    public Position multiplyByScalar(double scalar) {
        return new Position(this.x * scalar, this.y * scalar);
    }

    /**
     * Adds a position to another.
     *
     * @param position other position
     * @return resulting position
     */
    public Position plus(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

}
