package hu.miskolc.uni.robosoccer.core;

import lombok.*;

/**
 * Represents a connected user.
 *
 * @author Attila Szőke
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    private String sessionId;
    private String name;
}
