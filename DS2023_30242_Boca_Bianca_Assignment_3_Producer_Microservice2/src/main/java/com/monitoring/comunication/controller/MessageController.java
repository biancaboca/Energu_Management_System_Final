package com.monitoring.comunication.controller;

import com.monitoring.comunication.dto.DevicepPayLoadDto;
import com.monitoring.comunication.implementation.CSVService;
import com.monitoring.comunication.implementation.MessageService;
import com.monitoring.comunication.implementation.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/message")
public class MessageController {
    private final RabbitTemplate rabbitTemplate;
    private final CSVService csvService;
    private MessageService messageService;


    public MessageController(RabbitTemplate rabbitTemplate, CSVService csvService, MessageService messageService) {
        this.rabbitTemplate = rabbitTemplate;
        this.csvService = csvService;
        this.messageService=messageService;
    }

    @PostMapping("/registerJSON")
    public ResponseEntity<Map<String, String>> registerDevice(@RequestBody DevicepPayLoadDto devicePayloadDto) throws IOException {
        // Assuming that the timestamp is part of the DTO, otherwise use new Date() as before
        messageService.sendLatestMeasurementToQueue(devicePayloadDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Device payload registered successfully!");

        return ResponseEntity.ok(response);
    }



}
