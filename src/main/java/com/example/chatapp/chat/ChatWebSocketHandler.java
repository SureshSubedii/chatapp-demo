package com.example.chatapp.chat;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Integer, List<WebSocketSession>> clientSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer userId = getClientId(session);
        clientSessions
                .computeIfAbsent(userId, k -> new ArrayList<>())
                .add(session);
        System.out.println(clientSessions.get(userId));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDataDto data = objectMapper.readValue(payload, MessageDataDto.class);
        List<WebSocketSession> targetSessions = clientSessions.get(data.getTo());


        for (WebSocketSession s : targetSessions) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
       clientSessions.get(getClientId(session)).remove(session);

    }

    private Integer getClientId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("userId=")) {
            return Integer.parseInt(query.substring("userId=".length()));
        }
        return null;
    }
}
