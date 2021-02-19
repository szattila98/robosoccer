package hu.miskolc.uni.robosoccer.service;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;

/**
 * Defines the game operations of the application.
 *
 * @author Attila Szőke
 */
public interface GameService {

    void join(User user) throws MatchFullException;
    
}
