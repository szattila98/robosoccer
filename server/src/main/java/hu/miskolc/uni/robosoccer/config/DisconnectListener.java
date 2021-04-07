package hu.miskolc.uni.robosoccer.config;

import hu.miskolc.uni.robosoccer.core.Match;
import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import hu.miskolc.uni.robosoccer.core.exceptions.NoSuchUserException;
import hu.miskolc.uni.robosoccer.core.messages.outbound.UserConnectionStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

/**
 * Listens to disconnect events and sends a message about it to the clients. Also handles match logic, resets it on disconnect, etc.
 *
 * @author Attila Szőke
 */
@Slf4j
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
        try {
            User user = Match.getInstance().getJoinedUser(sha.getSessionId());
            Match.getInstance().reset();
            template.convertAndSend("/socket/join", new UserConnectionStateMessage(user, new Date(), ConnectionType.DISCONNECTED));
            log.warn("A user with sessionId: {} and name: {} disconnected, resetting match!", user.getSessionId(), user.getName());
        } catch (NoSuchUserException e) {
            log.error(e.getMessage() + " {}", sha.getSessionId());
        }
    }
}
