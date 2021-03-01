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

    abstract void calculatePositions(Position start, Position end);
}
