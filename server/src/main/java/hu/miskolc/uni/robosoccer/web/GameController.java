package hu.miskolc.uni.robosoccer.web;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.enums.ReadyType;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchOngoingException;
import hu.miskolc.uni.robosoccer.core.exceptions.UserNotReadyException;
import hu.miskolc.uni.robosoccer.core.messages.ConnectionMessage;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.core.messages.MatchStateMessage;
import hu.miskolc.uni.robosoccer.core.messages.StartMessage;
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

    @Scheduled(fixedRate = 50)
    public void sendMatchState() {
        if(Match.getInstance().getRoundStatusType() == RoundStatusType.ONGOING) {
            template.convertAndSend("/socket/game", new MatchStateMessage(Match.getInstance()));
            log.info("Match state sent");
        }
    }


    /**
     * Joins a player to the game.
     *
     * @param sha  the header accessor
     * @param name the player name
     */
    @MessageMapping("/join")
    public void join(SimpMessageHeaderAccessor sha, @Payload String name) {
        User user = new User(sha.getSessionId(), name);
        try {
            service.join(user);
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.CONNECTED));
            log.info("User: {} joined!", user);
        } catch (MatchFullException e) {
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.REFUSED));
            log.info("User: {} join refused!", user);
        }
    }

    @MessageMapping("/start")
    public void startMatch(@Payload User user) {
        try{
            service.startMatch(user);
            template.convertAndSend("/socket/game", new StartMessage(user, ReadyType.READY, RoundStatusType.ONGOING));
            log.info("User {} is ready, game is starting!", user);
        } catch (MatchOngoingException e) {
            template.convertAndSend("/socket/game", new StartMessage(user, ReadyType.READY, RoundStatusType.ONGOING));
            log.info("User {} is ready, but the game has already started!", user);
        } catch (UserNotReadyException e) {
            template.convertAndSend("/socket/game", new StartMessage(user, ReadyType.READY, RoundStatusType.PENDING));
            log.info("User {} is ready, but the opponent is still waiting!", user);
        }
    }

}
