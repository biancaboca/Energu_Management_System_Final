package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.ChatMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        if (query != null) {
            String userId = query.split("=")[1];
            sessions.put(session.getId(), session);
            userSessions.put(userId, session.getId());
            System.out.println("Session created: ID = " + session.getId() + " User = " + userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        System.out.println("Received message: " + chatMessage.getText() + " from: " + chatMessage.getFrom() + " to: " + chatMessage.getTo());

        if (chatMessage.isTyping()) {
            sendMessageToUser(chatMessage.getTo(), chatMessage);
        } else if (chatMessage.isSeen()) {
            sendSeenMessage(chatMessage);
        } else {
            sendMessageToUser(chatMessage.getTo(), chatMessage);
        }
    }

    private void sendMessageToUser(String userId, ChatMessage chatMessage) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String targetSessionId = userSessions.get(userId);
        if (targetSessionId != null) {
            WebSocketSession targetSession = sessions.get(targetSessionId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                System.out.println("Message sent.");
            } else {
                System.out.println("Target session is not open or does not exist.");
            }
        } else {
            System.out.println("No session found for user: " + userId);
        }
    }

    private void sendSeenMessage(ChatMessage chatMessage) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String targetSessionId = userSessions.get(chatMessage.getTo());
        if (targetSessionId != null) {
            WebSocketSession targetSession = sessions.get(targetSessionId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                System.out.println("Seen message sent.");
                System.out.println("seen :  " + chatMessage.isSeen());
            } else {
                System.out.println("Target session is not open or does not exist.");
            }
        } else {
            System.out.println("No session found for user: " + chatMessage.getTo());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        userSessions.values().remove(session.getId());
        System.out.println("Session closed: ID = " + session.getId());
    }
}
