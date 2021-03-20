package hu.miskolc.uni.robosoccer.service.impl;

import hu.miskolc.uni.robosoccer.core.*;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.exceptions.*;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePlayer(String sessionId, int playerId, Position destination) throws NoSuchUserException, PlayerNotFoundException, MatchNotGoingException {
        if (Match.getInstance().getRoundStatus() == RoundStatusType.PENDING) {
            throw new MatchNotGoingException();
        }
        // TODO check if destination is inside the pitch, if not make it so it is inside at the sideline
        Player player = Match.getInstance().getJoinedUser(sessionId).getPlayerById(playerId);
        player.plotPositionsToMoveTo(player.getPosition(), destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kickBall(Position direction, double kickForce, String sessionId) throws MatchNotGoingException, NoSuchUserException, KickNotAllowedException {
        if (Match.getInstance().getRoundStatus() == RoundStatusType.PENDING) {
            throw new MatchNotGoingException();
        }
        Ball ball = Match.getInstance().getBall();
        if (ball.getPlayer() == null || !Match.getInstance().checkIfUserTeamHasBall(sessionId)) {
            throw new KickNotAllowedException();
        }
        // TODO check if position is a valid point at the pitch sideline -> has no meaning if kick changes
        ball.setPlayer(null);
        ball.setForceOfKick(kickForce);
        ball.plotPositionsToMoveTo(ball.getPosition(), direction);
    }


}
