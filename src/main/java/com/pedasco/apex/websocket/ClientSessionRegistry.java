package com.pedasco.apex.websocket;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientSessionRegistry {

    // key: clientId, value: WebSocketSession
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(String clientId, WebSocketSession session) {
        sessions.put(clientId, session);
    }

    public void remove(String clientId) {
        sessions.remove(clientId);
    }

    public Optional<WebSocketSession> getSession(String clientId) {
        return Optional.ofNullable(sessions.get(clientId));
    }

    public Map<String, WebSocketSession> getAllSessions() {
        return Collections.unmodifiableMap(sessions);
    }

    public boolean isOnline(String clientId) {
        WebSocketSession session = sessions.get(clientId);
        return session != null && session.isOpen();
    }
}