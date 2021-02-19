package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.enums.SideType;
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
    private RoundStatusType roundStatusType;

    private Match() {
        this.users = new HashMap<>();
        this.ball = new Ball();
        this.roundNo = 0;
        this.roundStatusType = RoundStatusType.PENDING;
    }

    public static Match getInstance() {
        return instance;
    }

    public void joinPlayer(User user) throws MatchFullException {
        if (users.size() < 2) {
            if (users.size() == 0) {
                user.setSide(SideType.LEFT);
            } else {
                user.setSide(SideType.RIGHT);
            }
            users.put(user.getSessionId(), user);
        } else {
            throw new MatchFullException();
        }
    }

    public void advanceRound() {
        roundNo++;
    }

    public void setRoundStatusType(RoundStatusType roundStatusType) {
        this.roundStatusType = roundStatusType;
    }
}
