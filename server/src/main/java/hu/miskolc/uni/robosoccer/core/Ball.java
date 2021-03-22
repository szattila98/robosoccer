package hu.miskolc.uni.robosoccer.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.miskolc.uni.robosoccer.core.enums.SideType;
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
        this.positionsToMoveTo.clear();
        double distance = (Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2)))*forceOfKick;
        Position vector = start.toNormalizedDirectionVector(end);
        for (double i = 0; i < distance; i += forceOfKick) {
            Position newPosition = vector.multiplyByScalar(i).plus(this.position);
            if(!super.validatePosition(newPosition)) {
                break;
            }
            this.positionsToMoveTo.add(newPosition);
        }
        forceOfKick = null;
    }

    @Override
    public void processMovement() {
        if (player == null) {
            if (!this.positionsToMoveTo.isEmpty()) {
                Position newPosition = this.getPositionsToMoveTo().remove();
                checkGoal(newPosition);
                this.position.move(newPosition);
            }
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

    private void checkGoal(Position position) {
        if(position.getX() <= Match.GOAL_LINE  && position.getY() >= 30 && position.getY() <= 70) {
            for(User u : Match.getInstance().getUsers()) {
                if(u.getSide() == SideType.RIGHT) {
                    u.incrementPoints();
                    Match.getInstance().startNextRound();
                }
            }
        }
        else if(position.getX() >= Match.PITCH_WIDTH - Match.GOAL_LINE  && position.getY() >= 30 && position.getY() <= 70) {
            for(User u : Match.getInstance().getUsers()) {
                if(u.getSide() == SideType.LEFT){
                    u.incrementPoints();
                    Match.getInstance().startNextRound();
                }
            }
        }
    }

    public void recenterBall() {
        this.positionsToMoveTo.clear();
        this.forceOfKick = null;
        this.position.move(new Position(70, 50));
    }


    private Position positionByKickForce(Position start, Position end) {
        double newX = start.getX() + round((end.getX() - start.getX()) * forceOfKick, 1);
        double newY = start.getY() + round((end.getY() - start.getY()) * forceOfKick, 1);
        forceOfKick = null;
        return new Position(newX, newY);
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }



}
