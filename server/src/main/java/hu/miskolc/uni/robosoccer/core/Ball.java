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

    private static final double AHEAD_OF_PLAYER_DISTANCE = 3.5;

    private Player player; // null if no one has it
    @JsonIgnore
    private Double forceOfKick;

    public Ball() {
        super(new Position(70, 50)); // center of the soccer pitch
        this.player = null;
        this.forceOfKick = null;
    }

    @Override
    public void plotPositionsToMoveTo(Position start, Position end) {
        end = positionByKickForce(start, end);
        super.plotPositionsToMoveTo(start, end);
    }

    @Override
    public void processMovement() {
        if (player == null) {
            super.processMovement();
        } else {
            moveInFrontOfPlayer();
        }
    }

    private void moveInFrontOfPlayer() {
        if (this.player.positionsToMoveTo.size() >= 2) {
            Position playerPosition = this.player.getPositionsToMoveTo().get(0);
            Position playerNextPosition = this.player.getPositionsToMoveTo().get(1);
            Position vector = playerPosition.toNormalizedDirectionVector(playerNextPosition);
            Position ballPosition = vector.multiplyByScalar(AHEAD_OF_PLAYER_DISTANCE).plus(playerPosition);
            this.position.move(ballPosition);
        }
    }

    private Position positionByKickForce(Position start, Position end) {
        double newX = end.getX() + round((end.getX() - start.getX()) * this.forceOfKick, 1);
        double newY = end.getY() + round((end.getY() - start.getY()) * this.forceOfKick, 1);
        this.forceOfKick = null;
        return new Position(newX, newY);
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
