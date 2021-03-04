package hu.miskolc.uni.robosoccer.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.Queue;

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

    protected static final double STEP = 0.1;

    protected final Position position;
    @JsonIgnore
    protected final Queue<Position> positionsToMoveTo;

    protected Movable(Position startingPosition) {
        this.position = startingPosition;
        this.positionsToMoveTo = new LinkedList<>();
    }

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
    protected abstract void plotPositionsToMoveTo(Position start, Position end);
}
