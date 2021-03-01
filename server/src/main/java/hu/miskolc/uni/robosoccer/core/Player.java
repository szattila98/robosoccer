package hu.miskolc.uni.robosoccer.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents the soccer players.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Player extends Movable {

    private final int id;

    public Player(int id, Position startingPosition) {
        super(startingPosition);
        this.id = id;
    }

    /**
     * Calculates positions between the given two positions and stores them in start queue for processing (queue is cleared before storage).
     * Uses Bresenham’s line algorithm.
     *
     * @param start the starting position
     * @param end   the end position
     */
    @Override
    public void calculatePositions(Position start, Position end) {
        // TODO calculation not correct, x is fine but it disregards y and runs over it
        double degreeOfDetail = 0.25; // makes movement slower of faster
        double deltaX = end.getX() - start.getX();
        double deltaY = end.getY() - start.getY();
        double deltaErr = Math.abs(deltaY / deltaX);
        double error = 0.0;
        double y = start.getY();
        this.positionsToMoveTo.clear();
        for (double x = start.getX(); x < end.getX(); x += degreeOfDetail) {
            this.positionsToMoveTo.add(new Position(x, y));
            error = error + deltaErr;
            if (error >= 0.5) {
                y = y + Math.signum(deltaY);
                error = error - 1.0;
            }
        }
        // this.positionsToMoveTo.add(end); - uncomment when it is correctly calculating, as it leaves out the destination position
    }
}
