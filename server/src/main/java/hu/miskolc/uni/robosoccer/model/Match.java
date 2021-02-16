package hu.miskolc.uni.robosoccer.model;

import hu.miskolc.uni.robosoccer.core.User;
import lombok.*;

import java.util.List;

/**
 * Class stores the match datas: ID, ball, players of the two team, goals, and the two user
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Match {

    private int matchId;
    private User userA;
    private User userB;
    private Ball ball;
    private List<Player> playersA;
    private List<Player> playersB;
    private int pointsA;
    private int pointsB;

}
