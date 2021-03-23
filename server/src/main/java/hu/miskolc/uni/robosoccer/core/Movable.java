package hu.miskolc.uni.robosoccer.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;

/**
 * Represents and object that can be moved.
 * It should be inherited by moving objects.
 *
 * @author Attila Szőke
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class Movable {

    private static final double SPEED = 0.7;

    protected final Position position;
    @JsonIgnore
    protected final LinkedList<Position> positionsToMoveTo;

    /**
     * Initializes a movable.
     *
     * @param startingPosition the starting position
     */
    protected Movable(Position startingPosition) {
        this.position = startingPosition;
        this.positionsToMoveTo = new LinkedList<>();
    }

    /**
     * Moves th movable to its next position on its path.
     */
    public void processMovement() {
        if (!this.positionsToMoveTo.isEmpty()) {
            Position newPosition = this.getPositionsToMoveTo().remove();
            this.position.move(newPosition);
        }
    }

    /**
     * Calculates positions between the given two positions and stores them in start queue for processing (queue is cleared before storage).
     *
     * @param start the starting position
     * @param end   the end position
     */
    public void plotPositionsToMoveTo(Position start, Position end) {
        this.positionsToMoveTo.clear();
        double distance = Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
        Position vector = start.toNormalizedDirectionVector(end);
        for (double i = 0; i < distance; i += SPEED) {
            Position newPosition = vector.multiplyByScalar(i).plus(this.position);
            if (!validatePosition(newPosition)) {
                break;
            }
            this.positionsToMoveTo.add(newPosition);
        }
    }

    /**
     * Validates whether the position is within bounds.
     *
     * @param position position to validate
     * @return boolean
     */
    public boolean validatePosition(Position position) {
        if (position.getX() > Match.PITCH_WIDTH || position.getX() < 0) {
            return false;
        } else if (position.getY() > Match.PITCH_HEIGHT || position.getY() < 0) {
            return false;
        } else return true;
    }

}
