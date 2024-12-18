package com.monitoring.comunication.implementation;

import com.monitoring.comunication.Service.MessageConsumer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MonitoringDeviceUpdater {

    private final MonitoringDeviceService monitoringDeviceService; // Replace with your actual service
    private final MessageConsumer messageConsumer; // Assuming this is where measurements are held

    @Autowired
    public MonitoringDeviceUpdater(MonitoringDeviceService monitoringDeviceService, MessageConsumer messageConsumer) {
        this.monitoringDeviceService = monitoringDeviceService;
        this.messageConsumer = messageConsumer;
    }


}
