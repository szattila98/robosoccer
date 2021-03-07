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



}
