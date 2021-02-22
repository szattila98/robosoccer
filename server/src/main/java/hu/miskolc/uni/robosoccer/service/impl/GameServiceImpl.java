package hu.miskolc.uni.robosoccer.service.impl;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ReadyType;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchOngoingException;
import hu.miskolc.uni.robosoccer.core.exceptions.UserNotReadyException;
import hu.miskolc.uni.robosoccer.service.GameService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Basic implementation of the GameService interface.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Service
public class GameServiceImpl implements GameService {

    @Override
    public void join(User user) throws MatchFullException {
        Match.getInstance().joinPlayer(user);
        Match.getInstance().getUsers().forEach((i, u) -> u.fillTeam());
    }

    @Override
    public void startMatch(User user) throws MatchOngoingException, UserNotReadyException {
        if(Match.getInstance().getRoundStatusType() == RoundStatusType.ONGOING) {
            throw new MatchOngoingException();
        }

        Match.getInstance().getUsers().get(user.getSessionId()).setReadyType(true);

        if(checkUserReady()) {
            Match.getInstance().setRoundStatusType(RoundStatusType.ONGOING);
        }
        else {
            throw new UserNotReadyException();
        }
    }

    private boolean checkUserReady() {
        for(Map.Entry<String,User> entry : Match.getInstance().getUsers().entrySet()) {
            if(!entry.getValue().isReadyType()) {
                return false;
            }
        }
        return true;
    }

}
