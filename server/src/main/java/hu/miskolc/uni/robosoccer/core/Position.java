package hu.miskolc.uni.robosoccer.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a position on the soccer field.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Position {

    private int x;
    private int y;

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}
