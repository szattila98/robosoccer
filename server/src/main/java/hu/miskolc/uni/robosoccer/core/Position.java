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

    private double x;
    private double y;

    public void move(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public Position invert() {
        return new Position(Match.PITCH_WIDTH - this.x, this.y);
    }
}
