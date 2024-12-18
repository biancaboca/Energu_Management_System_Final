package com.monitoring.comunication.Service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {

    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    public void addUser(String sessionId, String userId) {
        userSessions.put(sessionId, userId);
    }

    public String getUserId(String sessionId) {
        return userSessions.get(sessionId);
    }

    public void removeUser(String sessionId) {
        userSessions.remove(sessionId);
    }

    // Additional methods as needed
}
