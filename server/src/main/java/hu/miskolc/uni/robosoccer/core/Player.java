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

    @Override
    public void plotPositionsToMoveTo(Position start, Position end) {
        this.positionsToMoveTo.clear();
        double slope = ((end.getY() - start.getY()) / (end.getX() - start.getX()));
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
    }

    private double pointSlope(Position point, double slope, double x) {
        return slope * (x - point.getX()) + point.getY();
    }
}
