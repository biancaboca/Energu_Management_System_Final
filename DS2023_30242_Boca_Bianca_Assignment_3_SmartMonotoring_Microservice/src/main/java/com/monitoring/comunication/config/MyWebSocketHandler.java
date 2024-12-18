package com.monitoring.comunication.config;

import com.monitoring.comunication.Service.MessageConsumer;
import com.monitoring.comunication.Service.UserSessionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class MyWebSocketHandler extends TextWebSocketHandler {

    private final UserSessionService userSessionService;
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    @Getter
    private volatile String lastSessionId; // To store the ID of the last connected session
    @Getter
    private volatile String userID; // To store the ID of the last connected session

    @Autowired
    public MyWebSocketHandler(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Assuming the entire payload is the userId
        this.userID = message.getPayload();
        userSessionService.addUser(session.getId(), userID);
        System.out.println("Received userId: " + userID);
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        lastSessionId = session.getId();

        String sessionId = session.getId();
        System.out.println("Session ID: " + sessionId);
        // You can store this sessionId along with associated user information if needed
    }
    public void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        userSessionService.removeUser(session.getId());

    }

    public String getUserId()
    {
        return this.userID;
    }

}
