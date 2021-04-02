package hu.miskolc.uni.robosoccer.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Random;

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

    /**
     * Constructor which initializes the ball, and centers it.
     */
    public Ball() {
        super(new Position(Match.PITCH_WIDTH / 2, Match.PITCH_HEIGHT / 2));
        this.player = null;
        this.forceOfKick = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void plotPositionsToMoveTo(Position start, Position end) {
        this.positionsToMoveTo.clear();
        double distance = (Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2))) * forceOfKick;
        Position vector = start.toNormalizedDirectionVector(end);
        for (double i = 0; i < distance; i += forceOfKick) {
            Position newPosition = vector.multiplyByScalar(i).plus(this.position);
            if (!super.validatePosition(newPosition)) {
                break;
            }
            this.positionsToMoveTo.add(newPosition);
        }
        forceOfKick = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processMovement() {
        if (player == null) {
            super.processMovement();
        } else {
            moveInFrontOfPlayer();
        }
    }

    /**
     * Moves the ball in front of the player who has it.
     */
    private void moveInFrontOfPlayer() {
        if (this.player.positionsToMoveTo.size() >= 2) {
            Position playerPosition = this.player.getPositionsToMoveTo().get(0);
            Position playerNextPosition = this.player.getPositionsToMoveTo().get(1);
            Position vector = playerPosition.toNormalizedDirectionVector(playerNextPosition);
            Position ballPosition = vector.multiplyByScalar(AHEAD_OF_PLAYER_DISTANCE).plus(playerPosition);
            this.position.move(ballPosition);
        }
    }

    /**
     * Gives a random starting position to the ball.
     */
    public void randomBallPosition() {
        int max = 25;
        int min = -25;
        int rand = new Random().nextInt(max - min) + min;
        this.positionsToMoveTo.clear();
        this.forceOfKick = null;
        this.position.move(new Position(Match.PITCH_WIDTH / 2 + rand, Match.PITCH_HEIGHT / 2 + rand));
    }
}
