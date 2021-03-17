package hu.miskolc.uni.robosoccer.web;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.exceptions.*;
import hu.miskolc.uni.robosoccer.core.messages.inbound.KickMessage;
import hu.miskolc.uni.robosoccer.core.messages.inbound.MoveMessage;
import hu.miskolc.uni.robosoccer.core.messages.outbound.MatchStateMessage;
import hu.miskolc.uni.robosoccer.core.messages.outbound.UserConnectionStateMessage;
import hu.miskolc.uni.robosoccer.core.messages.outbound.UserReadyStateMessage;
import hu.miskolc.uni.robosoccer.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Main controller of the application.
 *
 * @author Attila Szőke
 */
@RestController
@Slf4j
@EnableScheduling
@Configuration
public class GameController {

    private final SimpMessagingTemplate template;
    private final GameService service;

    @Autowired
    public GameController(SimpMessagingTemplate template, GameService service) {
        this.template = template;
        this.service = service;
    }

    @MessageMapping("/join")
    public void join(SimpMessageHeaderAccessor sha, @Payload String name) {
        User user = new User(sha.getSessionId(), name);
        try {
            service.join(user);
            template.convertAndSend("/socket/join", new UserConnectionStateMessage(user, new Date(), ConnectionType.CONNECTED));
            log.info("User: {} joined!", user);
        } catch (MatchFullException e) {
            template.convertAndSend("/socket/join", new UserConnectionStateMessage(user, new Date(), ConnectionType.REFUSED));
            log.info(e.getMessage() + " {}", user);
        }
    }

    @MessageMapping("/ready")
    public void toggleReady(SimpMessageHeaderAccessor sha) {
        User user = null;
        try {
            user = Match.getInstance().getJoinedUser(sha.getSessionId());
            service.toggleReady(user);
            template.convertAndSend("/socket/ready", new UserReadyStateMessage(user, user.isReady(), Match.getInstance().getRoundStatus()));
            log.info("User {} toggled ready!", user);
        } catch (MatchOngoingException e) {
            log.warn(e.getMessage() + " {}", user);
        } catch (NoSuchUserException e) {
            log.error(e.getMessage() + " {}", sha.getSessionId());
        }
    }

    @MessageMapping("/move")
    public void move(SimpMessageHeaderAccessor sha, @Payload MoveMessage message) {
        try {
            service.movePlayer(sha.getSessionId(), message.getPlayerId(), message.getDestination());
            log.info("Movement {} started!", message);
        } catch (NoSuchUserException e) {
            log.error(e.getMessage() + " {}", sha.getSessionId());
        } catch (PlayerNotFoundException e) {
            log.error(e.getMessage() + " {}", message.getPlayerId());
        } catch (MatchNotOnGoingException e) {
            log.error(e.getMessage());
        }
    }

    @MessageMapping("/kick")
    public void kick(SimpMessageHeaderAccessor sha, @Payload KickMessage message) {
        try {
            service.kickBall(message.getDestination(), message.getForceOfKick(), sha.getSessionId());
        } catch (MatchNotOnGoingException | NoSuchUserException | KickNotAllowedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sends the match state to the clients with the updated game logic.
     */
    @Scheduled(fixedRate = 50)
    public void sendMatchState() {
        if (Match.getInstance().getRoundStatus() == RoundStatusType.ONGOING) {
            Match.getInstance().processMovements();
            Match.getInstance().checkForBallCaptureEvent();
            Match.getInstance().checkGoal();
            template.convertAndSend("/socket/game", new MatchStateMessage(Match.getInstance()));
            log.debug("Match state sent: {}", Match.getInstance());
        }
    }

}
