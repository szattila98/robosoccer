package hu.miskolc.uni.robosoccer.service;

import hu.miskolc.uni.robosoccer.core.Position;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.exceptions.*;

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

    /**
     * Moves a player.
     *
     * @param sessionId   the user's session id
     * @param playerId    the player to move
     * @param destination the destination to move to
     * @throws NoSuchUserException     the no such user exception
     * @throws PlayerNotFoundException the player not found exception
     */
    void movePlayer(String sessionId, int playerId, Position destination) throws NoSuchUserException, PlayerNotFoundException, MatchNotGoingException;

    /**
     * Kicks the ball.
     *
     * @param direction the direction, a position at the edge of the pitch
     * @param kickForce force of the kick
     */
    void kickBall(Position direction, double kickForce) throws MatchNotGoingException;

}
