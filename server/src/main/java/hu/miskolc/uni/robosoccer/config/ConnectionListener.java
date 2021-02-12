package hu.miskolc.uni.robosoccer.config;

import hu.miskolc.uni.robosoccer.core.Connection;
import hu.miskolc.uni.robosoccer.core.enums.ConnectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectionListener {

    public static final Map<String, Connection> CONNECTIONS = new HashMap<>();

    private final SimpMessagingTemplate template;

    @Autowired
    public ConnectionListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void connectHandler(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        CONNECTIONS.put(sha.getSessionId(), new Connection(sha.getSessionId(), new Date(), null));
    }

    @EventListener
    public void subscribeHandler(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Connection conn = CONNECTIONS.get(sha.getSessionId());
        conn.setConnectionType(ConnectionType.CONNECTED);
        template.convertAndSend("/socket/connections", conn);
    }

    @EventListener
    public void disconnectHandler(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Connection conn = CONNECTIONS.get(sha.getSessionId());
        conn.setConnectionType(ConnectionType.DISCONNECTED);
        CONNECTIONS.remove(sha.getSessionId());
        template.convertAndSend("/socket/connections", conn);
    }
}
