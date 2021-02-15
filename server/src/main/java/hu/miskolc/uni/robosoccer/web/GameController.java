package hu.miskolc.uni.robosoccer.web;

import hu.miskolc.uni.robosoccer.core.ConnectionMessage;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Main controller of the application.
 *
 * @author Attila Szőke
 */
@RestController
public class GameController {

    private static final int MAX_USER_COUNT = 2;
    public static final Map<String, User> USERS = new HashMap<>();

    private final SimpMessagingTemplate template;

    @Autowired
    public GameController(SimpMessagingTemplate template) {
        this.template = template;
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
        if (USERS.size() < MAX_USER_COUNT) {
            GameController.USERS.put(sha.getSessionId(), user);
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.CONNECTED));
        } else {
            template.convertAndSend("/socket/game", new ConnectionMessage(user, new Date(), ConnectionType.REFUSED));
        }
    }
}
