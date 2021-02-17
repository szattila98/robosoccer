package hu.miskolc.uni.robosoccer.web;

import hu.miskolc.uni.robosoccer.core.ConnectionMessage;
import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import hu.miskolc.uni.robosoccer.core.exceptions.MatchFullException;
import hu.miskolc.uni.robosoccer.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Main controller of the application.
 *
 * @author Attila Szőke
 */
@RestController
@Slf4j
public class GameController {

    private final SimpMessagingTemplate template;
    private final GameService service;

    @Autowired
    public GameController(SimpMessagingTemplate template, GameService service) {
        this.template = template;
        this.service = service;
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
            Match.getInstance().getUsers().forEach((i, u) -> u.fillTeam());
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.CONNECTED));
            log.info("User: {} joined!", user);
        } catch (MatchFullException e) {
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.REFUSED));
            log.info("User: {} join refused!", user);
        }
    }
}
