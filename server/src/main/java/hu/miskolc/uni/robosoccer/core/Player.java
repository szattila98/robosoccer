package hu.miskolc.uni.robosoccer.core;

import lombok.*;

/**
 * Represents the soccer players.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Player {

    private final int id;
    private final Position position;
}
