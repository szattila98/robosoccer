package hu.miskolc.uni.robosoccer.core;

import lombok.*;

/**
 * Represents the soccer players.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Player {

    private int id;
    private Position position;
}
