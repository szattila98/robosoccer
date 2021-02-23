package hu.miskolc.uni.robosoccer.service;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchOngoingException;

/**
 * Defines the game operations of the application.
 *
 * @author Tamás Sólyom
 * @author Attila Szőke
 */
public interface GameService {

    /**
     * Joins a user to the match.
     *
     * @param user the user
     * @throws MatchFullException thrown when the match is full.
     */
    void join(User user) throws MatchFullException;

    /**
     * Toggles the ready state of the user and starts the game if every user has readied.
     *
     * @param user the user
     * @throws MatchOngoingException thrown when a user wants to toggle ready but the match is ongoing.
     */
    void toggleReady(User user) throws MatchOngoingException;

}
