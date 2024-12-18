package com.monitoring.comunication.controller;

import com.monitoring.comunication.dto.UserMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendNotification")
    @SendTo("/topic/notification")
    public String send(String message) {
        return message;
    }

    @MessageMapping("/userId")
    public void handleUserMessage(@Payload UserMessage userMessage)
    {
        String userId = String.valueOf(userMessage.getUserDevice());
        System.out.println("Received userId: " + userId);

    }
}
