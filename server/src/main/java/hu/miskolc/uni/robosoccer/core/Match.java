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

    private Match() {
        this.users = new ArrayList<>();
        this.ball = new Ball();
        this.roundNo = 0;
        this.roundStatus = RoundStatusType.PENDING;
    }

    public static Match getInstance() {
        if (instance == null) instance = new Match();
        return instance;
    }

    public void joinPlayer(User user) throws MatchFullException {
        if (this.users.size() < 2) {
            if (users.size() == 0) {
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

    public User getJoinedUser(String sessionId) throws NoSuchUserException {
        for (User u : Match.getInstance().getUsers()) {
            if (u.getSessionId().equals(sessionId))
                return u;
        }
        throw new NoSuchUserException();
    }

    public void advanceRound() {
        this.roundNo++;
    }

    public void setRoundStatus(RoundStatusType roundStatus) {
        this.roundStatus = roundStatus;
    }

    public void reset() {
        List<User> listWithConnectedUser = this.getUsers();
        instance = new Match();
        instance.getUsers().addAll(listWithConnectedUser);
    }

    public void startNextRound() {
        advanceRound();
        this.ball.recenterBall();
        for(User u : this.users) {
            u.fillTeam();
        }
        System.out.println("X:" + this.ball.getPosition().getX() + " Y:" + this.ball.getPosition().getY());
    }

    public boolean canStartMatch() {
        return this.users.stream().allMatch(User::isReady) && this.users.size() == 2;
    }

    public void processMovements() {
        this.ball.processMovement();
        this.users.forEach((user -> user.getTeam().forEach((Movable::processMovement))));
    }

    public void checkForBallCaptureEvent() {
        for (User user : this.users) {
            if (this.ball.getPlayer() == null || this.ball.getPlayer().getSide() != user.getSide()) {
                for (Player player : user.getTeam()) {
                    if (player.fallsInsidePlayerReach(this.ball.getPosition())) {
                        this.ball.setPlayer(player);
                    }
                }
            }
        }
    }

    public boolean checkIfUserTeamHasBall(String sessionId) throws NoSuchUserException {
        return this.getJoinedUser(sessionId).getSide() == this.ball.getPlayer().getSide();
    }

    public boolean checkGoal(Position position) {
        if(position.getX() <= GOAL_LINE  && position.getY() >= 30 && position.getY() <= 70) {
            for(User u : this.users) {
                if(u.getSide() == SideType.RIGHT) {
                    u.incrementPoints();
                    startNextRound();
                    return true;
                }
            }
        }
        else if(position.getX() >= PITCH_WIDTH - GOAL_LINE  && position.getY() >= 30 && position.getY() <= 70) {
            for(User u : this.users) {
                if(u.getSide() == SideType.LEFT){
                    u.incrementPoints();
                    startNextRound();
                    return true;
                }
            }
        }
        return false;
    }

}
