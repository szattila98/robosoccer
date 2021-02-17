package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.RoundStatus;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a singleton soccer match.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Getter
@ToString
@EqualsAndHashCode
public class Match {

    private static final Match instance = new Match();

    private final Map<String, User> users;
    private final Ball ball;
    private int roundNo;
    private RoundStatus roundStatus;

    private Match() {
        this.users = new HashMap<>();
        this.ball = new Ball();
        this.roundNo = 0;
        this.roundStatus = RoundStatus.PENDING;
    }

    public static Match getInstance() {
        return instance;
    }

    public void joinPlayer(User user) throws MatchFullException {
        if (users.size() < 2) {
            users.put(user.getSessionId(), user);
        } else {
            throw new MatchFullException();
        }
    }

    public void advanceRound() {
        roundNo++;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }
}
