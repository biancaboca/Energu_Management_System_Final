package com.monitoring.comunication.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.comunication.dto.DevicepPayLoadDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final CSVService csvService;
    private final ObjectMapper objectMapper;

    //private String uuidFilePath = "D:\\an4\\sem1\\sd\\sdTema2\\sdb\\DS2023_30242_Boca_Bianca_Assignment_2_Producer_Microservice1\\src\\main\\java\\com\\monitoring\\comunication\\config\\deviceID"; // Specify the path to your UUID file
    private String uuidFilePath = "src\\main\\java\\com\\monitoring\\comunication\\config\\deviceID"; // Specify the path to your UUID file

    private String queueName="stringDevice";

    public MessageService(RabbitTemplate rabbitTemplate, CSVService csvService, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.csvService = csvService;
        this.objectMapper = objectMapper;
    }


    private boolean isValidUUIDFormat(String uuidString) {
        String uuidRegex =
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        Pattern pattern = Pattern.compile(uuidRegex);
        return pattern.matcher(uuidString).matches();
    }
    private UUID readUUIDFromFile() throws IOException {
        Path filePath = Path.of(uuidFilePath);
        String uuidString = Files.readString(filePath).trim();

        // Check if the read string is a valid UUID format
        if (isValidUUIDFormat(uuidString)) {
            return UUID.fromString(uuidString);
        } else {
            return UUID.fromString("11111111-1111-1111-1111-111111111111");
        }
    }
    public void sendLatestMeasurementToQueue(DevicepPayLoadDto devicePayloadDto) throws IOException {
        Double latestMeasurement = csvService.getLatestMeasurement();
        UUID deviceId = readUUIDFromFile();

        if (latestMeasurement != null) {
            try {
                devicePayloadDto = new DevicepPayLoadDto(
                        deviceId,
                        new Date(),
                        latestMeasurement
                );

                String queuePayloadString = objectMapper.writeValueAsString(devicePayloadDto);
                rabbitTemplate.convertAndSend(queueName, queuePayloadString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize message", e);
            }
        } else {
            // Handle the case when there is no measurement available
            System.out.println("No measurements available to send to the queue.");
        }
    }

}
