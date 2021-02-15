package hu.miskolc.uni.robosoccer.config;

import hu.miskolc.uni.robosoccer.core.ConnectionMessage;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import hu.miskolc.uni.robosoccer.web.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

/**
 * Listens to disconnect events and sends a message about it to the clients.
 *
 * @author Attila Szőke
 */
@Component
public class DisconnectListener {

    private final SimpMessagingTemplate template;

    @Autowired
    public DisconnectListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void disconnectHandler(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        User user = GameController.USERS.get(sha.getSessionId());
        ConnectionMessage conn = new ConnectionMessage(user, new Date(), ConnectionType.DISCONNECTED);
        GameController.USERS.remove(sha.getSessionId());
        template.convertAndSend("/socket/game", conn);
    }
}
