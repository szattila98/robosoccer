package hu.miskolc.uni.robosoccer.model;

import lombok.*;

/**
 * Class stores the ID and position of the players on the field
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Player {

    private int id;
    private int x;
    private int y;

}
