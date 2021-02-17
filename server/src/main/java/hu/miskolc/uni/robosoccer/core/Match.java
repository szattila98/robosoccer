package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.RoundStatus;
import hu.miskolc.uni.robosoccer.core.exception.MatchFullException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the soccer match.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Match {

    private final Map<String, User> users;
    private final Ball ball;
    private int roundNo;
    private RoundStatus roundStatus;

    public Match() {
        this.users = new HashMap<>();
        this.ball = new Ball();
        this.roundNo = 0;
        this.roundStatus = RoundStatus.PENDING;
    }

    public void joinPlayer(User user) throws MatchFullException {
        if (users.size() < 2) {
            users.put(user.getSessionId(), user);
        } else {
            throw new MatchFullException();
        }
    }

}
