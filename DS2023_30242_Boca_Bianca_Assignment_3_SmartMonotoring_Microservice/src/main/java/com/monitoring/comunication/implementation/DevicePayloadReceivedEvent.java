package com.monitoring.comunication.implementation;

import com.monitoring.comunication.Service.DevicepPayLoadDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DevicePayloadReceivedEvent extends ApplicationEvent {
    private final DevicepPayLoadDto payload;

    public DevicePayloadReceivedEvent(Object source, DevicepPayLoadDto payload) {
        super(source);
        this.payload = payload;
    }

}
