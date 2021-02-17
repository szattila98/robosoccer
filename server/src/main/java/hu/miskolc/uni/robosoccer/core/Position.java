package hu.miskolc.uni.robosoccer.core;

import lombok.*;

/**
 * Represents a position on the soccer field.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Position {

    private int x;
    private int y;
}
