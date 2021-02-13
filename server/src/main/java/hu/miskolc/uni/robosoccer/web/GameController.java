package hu.miskolc.uni.robosoccer.web;

import hu.miskolc.uni.robosoccer.core.ConnectionMessage;
import hu.miskolc.uni.robosoccer.core.Player;
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

@RestController
public class GameController {

    private static final int MAX_PLAYER_COUNT = 2;
    public static final Map<String, Player> PLAYERS = new HashMap<>();

    private final SimpMessagingTemplate template;

    @Autowired
    public GameController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/join")
    public void join(SimpMessageHeaderAccessor sha, @Payload String name) {
        Player player = new Player(sha.getSessionId(), name);
        if (PLAYERS.size() < MAX_PLAYER_COUNT) {
            GameController.PLAYERS.put(sha.getSessionId(), player);
            template.convertAndSend("/socket/connections", new ConnectionMessage(player, new Date(), ConnectionType.CONNECTED));
        } else {
            template.convertAndSend("/socket/connections", new ConnectionMessage(player, new Date(), ConnectionType.REFUSED));
        }
    }
}
