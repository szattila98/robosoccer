package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.enums.SideType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.exceptions.NoSuchUserException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    public static final double PITCH_WIDTH = 140;
    public static final double PITCH_HEIGHT = 100;
    public static final double GOAL_LINE = 4;

    private static Match instance;

    private final List<User> users;
    private final Ball ball;
    private int roundNo;
    private RoundStatusType roundStatus;

    /**
     * Initializes a new match.
     */
    private Match() {
        this.users = new ArrayList<>();
        this.ball = new Ball();
        this.roundNo = 0;
        this.roundStatus = RoundStatusType.PENDING;
    }

    /**
     * Returns the match singleton instance.
     *
     * @return the match
     */
    public static Match getInstance() {
        if (instance == null) instance = new Match();
        return instance;
    }

    /**
     * Joins a user to the match.
     *
     * @param user the user
     * @throws MatchFullException thrown when the match is full
     */
    public void joinPlayer(User user) throws MatchFullException {
        if (this.users.size() < 2) {
            if (users.isEmpty()) {
                user.setSide(SideType.LEFT);
            } else {
                user.setSide(SideType.RIGHT);
            }
            user.fillTeam();
            this.users.add(user);
        } else {
            throw new MatchFullException();
        }
    }

    /**
     * Returns a joined user.
     *
     * @param sessionId the user's session id
     * @return the user
     * @throws NoSuchUserException thrown when no user with the sessionId is present
     */
    public User getJoinedUser(String sessionId) throws NoSuchUserException {
        for (User u : Match.getInstance().getUsers()) {
            if (u.getSessionId().equals(sessionId))
                return u;
        }
        throw new NoSuchUserException();
    }

    /**
     * Sets the round status.
     *
     * @param roundStatus new status
     */
    public void setRoundStatus(RoundStatusType roundStatus) {
        this.roundStatus = roundStatus;
    }

    /**
     * Resets the match.
     */
    public void reset() {
        instance = new Match();
    }

    /**
     * Checks if the match can be started.
     *
     * @return boolean
     */
    public boolean canStartMatch() {
        return this.users.stream().allMatch(User::isReady) && this.users.size() == 2;
    }

    /**
     * Processes the movements of the ball and players.
     */
    public void processMovements() {
        this.ball.processMovement();
        this.users.forEach((user -> user.getTeam().forEach((Movable::processMovement))));
    }

    /**
     * Checks whether th ball is captured by someone.
     */
    public void checkForBallCaptureEvent() {
        for (User user : this.users) {
            if (this.ball.getPlayer() == null || this.ball.getPlayer().getSide() != user.getSide()) {
                for (Player player : user.getTeam()) {
                    if (player.fallsInsidePlayerReach(this.ball.getPosition())) {
                        this.ball.setPlayer(player);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Checks if the team of a user has the ball.
     *
     * @param sessionId session id of the user
     * @return boolean
     * @throws NoSuchUserException thrown when no user with the sessionId is present
     */
    public boolean checkIfUserTeamHasBall(String sessionId) throws NoSuchUserException {
        return this.getJoinedUser(sessionId).getSide() == this.ball.getPlayer().getSide();
    }

    /**
     * Checks whether there is a goal event.
     */
    public void checkGoal() {
        Position ballPosition = this.ball.getPosition();
        if (ballPosition.getX() <= GOAL_LINE && ballPosition.getY() >= 30 && ballPosition.getY() <= 70) {
            for (User u : this.users) {
                if (u.getSide() == SideType.RIGHT) {
                    u.incrementPoints();
                    startNextRound();
                }
            }
        } else if (ballPosition.getX() >= PITCH_WIDTH - GOAL_LINE && ballPosition.getY() >= 30 && ballPosition.getY() <= 70) {
            for (User u : this.users) {
                if (u.getSide() == SideType.LEFT) {
                    u.incrementPoints();
                    startNextRound();
                }
            }
        }
    }

    /**
     * Starts the next round.
     */
    public void startNextRound() {
        advanceRound();
        this.ball.randomBallPosition();
        this.ball.setPlayer(null);
        for (User u : this.users) {
            u.fillTeam();
        }
    }

    /**
     * Advances round number.
     */
    public void advanceRound() {
        this.roundNo++;
    }

}
