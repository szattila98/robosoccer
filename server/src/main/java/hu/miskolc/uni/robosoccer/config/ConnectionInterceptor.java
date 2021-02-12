package hu.miskolc.uni.robosoccer.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ConnectionInterceptor implements HandshakeInterceptor {

    private static final int MAX_PLAYER_COUNT = 2;

    // TODO ijesztő errorok rákeres és az itteni és böngésző logban jobb megoldása elutasításnál

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) {
        return ConnectionListener.CONNECTIONS.size() < MAX_PLAYER_COUNT;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
