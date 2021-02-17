package hu.miskolc.uni.robosoccer.service;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exception.MatchFullException;

/**
 * Defines the game operations of the application.
 *
 * @author Attila Szőke
 */
public interface GameService {

    Match MATCH = new Match();

    void join(User user) throws MatchFullException;
}
