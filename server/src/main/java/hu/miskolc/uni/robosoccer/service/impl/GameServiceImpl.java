package hu.miskolc.uni.robosoccer.service.impl;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchOngoingException;
import hu.miskolc.uni.robosoccer.service.GameService;
import org.springframework.stereotype.Service;

/**
 * Basic implementation of the GameService interface.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
@Service
public class GameServiceImpl implements GameService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(User user) throws MatchFullException {
        Match.getInstance().joinPlayer(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleReady(User user) throws MatchOngoingException {
        if (Match.getInstance().getRoundStatus() == RoundStatusType.ONGOING) {
            throw new MatchOngoingException();
        }
        user.toggleReady();
        if (Match.getInstance().canStartMatch()) {
            Match.getInstance().setRoundStatus(RoundStatusType.ONGOING);
        }
    }

}
