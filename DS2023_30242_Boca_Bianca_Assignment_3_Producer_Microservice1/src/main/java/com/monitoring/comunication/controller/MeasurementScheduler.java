package com.monitoring.comunication.controller;

import com.monitoring.comunication.dto.DevicepPayLoadDto;
import com.monitoring.comunication.implementation.MessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MeasurementScheduler {

    private final MessageService messageService;

    public MeasurementScheduler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Scheduled(fixedRate = 10000)
    public void sendMeasurement() {
        try {
            DevicepPayLoadDto devicePayloadDto = new DevicepPayLoadDto(); // Construct as needed
            messageService.sendLatestMeasurementToQueue(devicePayloadDto);
        } catch (IOException e) {
            System.out.printf("Could not do this");
        }
    }
}
