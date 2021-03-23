package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.SideType;
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
    private final SideType side;

    /**
     * Initializes a new player.
     *
     * @param id               id
     * @param startingPosition starting position
     * @param side             side of the pitch
     */
    public Player(int id, Position startingPosition, SideType side) {
        super(startingPosition);
        this.id = id;
        this.radius = 2;
        this.side = side;
    }

    /**
     * Checks whether a position falls inside the players reach.
     *
     * @param otherPosition a position
     * @return boolean
     */
    public boolean fallsInsidePlayerReach(Position otherPosition) {
        // the inequation is (x - center_x)^2 + (y - center_y)^2 < radius^2
        // < inside the circle
        // <= inside and on the circle
        return Math.pow(otherPosition.getX() - this.position.getX(), 2) + Math.pow(otherPosition.getY() - this.position.getY(), 2)
                < Math.pow(radius, 2);
    }

}
