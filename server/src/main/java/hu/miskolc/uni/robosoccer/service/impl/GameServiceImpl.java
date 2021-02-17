package hu.miskolc.uni.robosoccer.service.impl;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exception.MatchFullException;
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

    @Override
    public void join(User user) throws MatchFullException {
        MATCH.joinPlayer(user);
    }
}
