package com.monitoring.comunication.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.comunication.config.MyWebSocketHandler;
import com.monitoring.comunication.dto.JsonDeleteValue;
import com.monitoring.comunication.dto.JsonInsertingValue;
import com.monitoring.comunication.dto.JsonUpdateValue;
import com.monitoring.comunication.dto.MeasurementDTO;
import com.monitoring.comunication.entity.MonitoringDevice;
import com.monitoring.comunication.implementation.DevicePayloadReceivedEvent;
import com.monitoring.comunication.implementation.MonitoringDeviceService;
import org.hibernate.validator.internal.engine.valueextraction.OptionalDoubleValueExtractor;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Collections;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Component
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final MonitoringDeviceService service;
    private MonitoringDevice monitoringDeviceInsert=new MonitoringDevice();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
    private final List<Double> measurements = new CopyOnWriteArrayList<>();
    private final ApplicationEventPublisher eventPublisher;
    private  DevicePayloadReceivedEvent event;
    private boolean hasRun = false;
    private boolean hasSending = false;
    private UUID valueIDFromProducer;


    private UUID deviceIdToUpdate; // Assuming you know which device to update
    private int maxHours=0;
    double sum = 0.0;
    private Double getLatest;

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;


    public MessageConsumer(ApplicationEventPublisher eventPublisher, MonitoringDeviceService monitoringDevice,  MyWebSocketHandler myWebSocketHandler) {
        this.eventPublisher = eventPublisher;
        this.service=monitoringDevice;
        this.myWebSocketHandler=myWebSocketHandler;

    }

    public void logMessageWithTimestamp(String message) {
        String timestamp = formatter.format(new Date());
        System.out.println("Message received at " + timestamp + ": " + message);
    }

    private String formatTimestamp(Date date) {
        return (date != null) ? formatter.format(date) : "null";
    }

    public List<Double> getAllMeasurements() {
        return Collections.unmodifiableList(measurements);
    }

/*
    @RabbitListener(queues = "insertDevice")
*/


    @RabbitListener(queues = "stringDevice")
    public void onMessageReceived(String message) {
        try {
            DevicepPayLoadDto payload = objectMapper.readValue(message, DevicepPayLoadDto.class);
            measurements.add(payload.getMeasurmentCode());
//            monitoringDeviceInsert.setAllMeasuringMeters(measurements);
//            monitoringDeviceInsert.setAllMeasuringMeters(measurements);
            monitoringDeviceInsert.setTimestamp(payload.getTimestamp());
            valueIDFromProducer = payload.getId();
            getLatest = payload.getMeasurmentCode();
            System.out.println(payload.getMeasurmentCode()+" messured");
            System.out.println(payload.getTimestamp()+" timestamp");
            System.out.println(payload.getId()+" id");


        } catch (Exception e) {
            logger.error("Error processing message", e);
        }
    }


    @RabbitListener(queues = "insertDevice")
    public void onMessageReceivedFromInsetDevice(String message) {
        try {
            JsonInsertingValue payload = objectMapper.readValue(message, JsonInsertingValue.class);
            // DevicepPayLoadDto payloadListening = event.getPayload();
/*            List<Double> measuringMeters = new ArrayList<>();
            measuringMeters.add(payloadListening.getMeasurmentCode());*/
            deviceIdToUpdate = payload.getIdDevice();

            monitoringDeviceInsert.setIdDevice(payload.getIdDevice());
            monitoringDeviceInsert.setIdPerson(payload.getIdUser());
            monitoringDeviceInsert.setMaxHours(payload.getMaxHours());
            maxHours = (int) payload.getMaxHours();
     /*       monitoringDevice.setTimestamp(null);
            monitoringDevice.setAllMeasuringMeters(measuringMeters);*/
            service.createMonitoringDeviceRabbit(monitoringDeviceInsert);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void calculate() {
        if (!hasRun) {
            List<MonitoringDevice> allDevices = service.getAllMonitoringDevices();
            for (MonitoringDevice device : allDevices) {
                if (device.getIdPerson().toString().equals(myWebSocketHandler.getUserId())) {
                    if (maxHours < sum) {
                        String message = "Threshold exceeded";
                        myWebSocketHandler.broadcastMessage(message);
                    }
                }
            }
            hasRun = true;
        }
    }


    @Scheduled(fixedRate = 10000) // 10,000 milliseconds = 10 seconds
    public void updateMeasurements() {
        List<MonitoringDevice> allDevices = service.getAllMonitoringDevices();
        Random random = new Random();
        if (valueIDFromProducer.equals(UUID.fromString("11111111-1111-1111-1111-111111111111"))) {

            for (MonitoringDevice device : allDevices) {

                List<Double> existingMeasurements = device.getAllMeasuringMeters();
                if (existingMeasurements == null) {
                    existingMeasurements = new ArrayList<>();
                }

                // Only add new measurements if there are less than 6
                int numberOfMeasurementsToAdd = Math.min(6 - existingMeasurements.size(), measurements.size());

                if(numberOfMeasurementsToAdd == 0)
                {
                    existingMeasurements.remove(5);
                    numberOfMeasurementsToAdd=1;
                }
                // Shuffle the available measurements and pick the required number
//                Collections.shuffle(measurements, random);
//                for (int i = 0; i < numberOfMeasurementsToAdd; i++) {
//                    existingMeasurements.add(measurements.get(i));
//                }
                while (true) {
                    existingMeasurements.add(measurements.get(random.nextInt(measurements.size())));

                    if (existingMeasurements.size() >= numberOfMeasurementsToAdd) {
                        break; // Exit the loop when the desired number of measurements is reached
                    }
                }//                if(existingMeasurements.size()>numberOfMeasurementsToAdd)
//                {
//                    continue;
//                }

                // Calculate the average if needed
                if (!existingMeasurements.isEmpty()) {
                    for (Double value : existingMeasurements) {
                        sum += value;
                        System.out.println("Sum is "+ sum + " value " +value +"\n" );
                    }
                    device.setMedieAritmetica(sum / existingMeasurements.size());
                    System.out.println("medie is "+ sum / existingMeasurements.size());
                }

                if (device.getIdPerson().toString().equals(myWebSocketHandler.getUserId())) {

                    if (!hasSending) {
                        myWebSocketHandler.broadcastMessage(device.getAllMeasuringMeters().toString());
                        hasSending = true;
                        break;
                    }

                    if (!hasRun) {
                        if (maxHours < sum) {
                            String message = "Threshold exceeded " + device.getIdDevice() + " with medie" + device.getMedieAritmetica() ;
                            myWebSocketHandler.broadcastMessage(message);
                            hasRun = true;
                            break;

                        }
                    }
                    hasRun = false;
                }

                service.getMeanForCurrentDevice(device.getIdDevice(), sum, maxHours);

                device.setAllMeasuringMeters(existingMeasurements);
                service.updateMonitoringDevice(device.getIdDevice(), device);
            }
        }
        else {
            MonitoringDevice device= service.findBYidDevice(valueIDFromProducer);
            List<Double> listDouble=new ArrayList<>();
            List<Double> existingMeasurements = device.getAllMeasuringMeters();
            if (existingMeasurements == null) {
                existingMeasurements = new ArrayList<>();
            }

            // Only add new measurements if there are less than 6
            int numberOfMeasurementsToAdd = Math.min(6 - existingMeasurements.size(), measurements.size());

            // Shuffle the available measurements and pick the required number
            if(numberOfMeasurementsToAdd == 0)
            {
                existingMeasurements.remove(0);
                numberOfMeasurementsToAdd=1;
            }
//            Collections.shuffle(measurements, random);
//            for (int i = 0; i < numberOfMeasurementsToAdd; i++) {
//                existingMeasurements.add(measurements.get(i));
//            }
            while (true) {
                existingMeasurements.add(measurements.get(random.nextInt()));

                if (existingMeasurements.size() >= numberOfMeasurementsToAdd) {
                    break; // Exit the loop when the desired number of measurements is reached
                }
            }
            // Calculate the average if needed
            if (!existingMeasurements.isEmpty()) {
                for (Double value : existingMeasurements) {
                    sum += value;
                    System.out.println("Sum is" + sum + " and value is " + value );
                }
                device.setMedieAritmetica(sum / existingMeasurements.size());
                System.out.println("medie is" + sum/existingMeasurements.size());
            }

            if (device.getIdPerson().toString().equals(myWebSocketHandler.getUserId())) {

                if (!hasSending) {
                    myWebSocketHandler.broadcastMessage(device.getAllMeasuringMeters().toString());
                    hasSending = true;
                }

                if (!hasRun) {
                    if (maxHours < sum) {
                        String message = "Threshold exceeded " + device.getIdDevice() + " with sum "+ device.getMedieAritmetica() ;
                        myWebSocketHandler.broadcastMessage(message);
                        hasRun = true;

                    }
                }
                hasRun = false;
            }

            service.getMeanForCurrentDevice(device.getIdDevice(), sum, maxHours);

            device.setAllMeasuringMeters(existingMeasurements);
            service.updateMonitoringDevice(device.getIdDevice(), device);
        }


        }

//        private List<MeasurementDTO> createMeasurementDTOs(List<Double> measurements) {
//        return measurements.stream()
//                .map(value -> new MeasurementDTO("Time-" + System.currentTimeMillis(), value))
//                .collect(Collectors.toList());
//    }

    private void broadcastMeasurements(List<MeasurementDTO> measurementDTOs) {
        try {
            String message = objectMapper.writeValueAsString(measurementDTOs);
            myWebSocketHandler.broadcastMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void runOnce() {
        if (!hasRun) {
            calculate();
            hasRun = true;
        }
    }




        @RabbitListener(queues = "updateDevice")
    public void onMessageReceivedFromUpdateDevice(String message) {
        try {
            JsonUpdateValue payload = objectMapper.readValue(message, JsonUpdateValue.class);

            System.out.println("Device id: " + payload.getIdDevice());
            System.out.println("Person ID: " + payload.getIdUser());
            System.out.println("Max Hours: " + payload.getMaxHours());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "deleteDevice")

    public void onMessageReceivedFromDeleteDevice(String message) {
        try {
            JsonDeleteValue payload = objectMapper.readValue(message, JsonDeleteValue.class);

            System.out.println("Device id: " + payload.getIdDevice());
            service.deleteMonitoringDevice(payload.getIdDevice());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @EventListener
    //    public void onDevicePayloadReceived(DevicePayloadReceivedEvent event) {
    //        DevicepPayLoadDto payload = event.getPayload();
    //        // Now you have access to the payload
    //        System.out.println(payload.getMeasurmentCode() + " measured");
    //        System.out.println(payload.getTimestamp() + " timestamp");
    //    }

}
