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
    private final int radius;

    public Player(int id, Position startingPosition) {
        super(startingPosition);
        this.id = id;
        this.radius = 1;
    }

    public boolean fallsInsidePlayerReach(Position otherPosition) {
        // the inequation is (x - center_x)^2 + (y - center_y)^2 < radius^2
        // < inside the circle
        // <= inside and on the circle
        return Math.pow(otherPosition.getX() - this.position.getX(), 2) + Math.pow(otherPosition.getY() - this.position.getY(), 2)
                < Math.pow(radius, 2);
    }

}
