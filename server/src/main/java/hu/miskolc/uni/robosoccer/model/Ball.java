package hu.miskolc.uni.robosoccer.model;

import lombok.*;

/**
 * Class stores the X and Y coordinates of the ball
 * PlayerId stores the ID of player who has the ball (value = 0 if no one has it)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Ball {

    private int x;
    private int y;
    private int playerId;

}
