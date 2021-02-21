package hu.miskolc.uni.robosoccer.service;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchOngoingException;
import hu.miskolc.uni.robosoccer.core.exceptions.UserNotReadyException;

/**
 * Defines the game operations of the application.
 *
 * @author Attila Szőke
 */
public interface GameService {

    void join(User user) throws MatchFullException;

    void startMatch(User user) throws MatchOngoingException, UserNotReadyException;
    
}
