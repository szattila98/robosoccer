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

    protected static final double STEP = 0.1;

    protected final Position position;
    @JsonIgnore
    protected final LinkedList<Position> positionsToMoveTo;

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
    public void plotPositionsToMoveTo(Position start, Position end) {
        this.positionsToMoveTo.clear();
        double slope;
        if (end.getX() != start.getX()) { // slope is normal
            slope = ((end.getY() - start.getY()) / (end.getX() - start.getX()));
            if (start.getX() < end.getX()) {
                for (double x = start.getX(); x <= end.getX(); x += STEP) {
                    Position newPosition = new Position(x, this.pointSlope(start, slope, x));
                    this.positionsToMoveTo.add(newPosition);
                }
            } else {
                for (double x = start.getX(); x >= end.getX(); x -= STEP) {
                    Position newPosition = new Position(x, this.pointSlope(start, slope, x));
                    this.positionsToMoveTo.add(newPosition);
                }
            }
        } else { // there is no slope as the movement is vertical
            if (start.getY() < end.getY()) {
                for (double y = start.getY(); y <= end.getY(); y += STEP) {
                    Position newPosition = new Position(start.getX(), y);
                    this.positionsToMoveTo.add(newPosition);
                }
            } else {
                for (double y = start.getY(); y >= end.getY(); y -= STEP) {
                    Position newPosition = new Position(start.getX(), y);
                    this.positionsToMoveTo.add(newPosition);
                }
            }
        }
    }

    protected double pointSlope(Position point, double slope, double x) {
        return slope * (x - point.getX()) + point.getY();
    }

}
