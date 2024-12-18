package com.monitoring.comunication.Service;

import com.monitoring.comunication.entity.MonitoringDevice;
import com.monitoring.comunication.repository.MonitoringDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MonotoringService {

    public WebSocketHandler webSocketHandler;
    public MonitoringDeviceRepository monitoringDeviceRepository;

    @Autowired
    public MonotoringService(WebSocketHandler webSocketHandler,MonitoringDeviceRepository monitoringDeviceRepository)
    {
        this.webSocketHandler=webSocketHandler;
        this.monitoringDeviceRepository=monitoringDeviceRepository;
    }




    public List<Double> getListMeasurement(UUID id)
    {
        MonitoringDevice monitoringDevice = monitoringDeviceRepository.findById(id).get();
        return monitoringDevice.getAllMeasuringMeters();
    }

    public List<MonitoringDevice> getListMonotoring()
    {
        List<MonitoringDevice> monitoringDevices = monitoringDeviceRepository.findAll();
        return  monitoringDevices;
    }

    public Date getDateAndOwnerOfDevice(UUID idOwner, UUID idDevice) {
        Optional<MonitoringDevice> monitoringDeviceOptional = monitoringDeviceRepository.findById(idDevice);
        if (monitoringDeviceOptional.isPresent()) {
            MonitoringDevice monitoringDevice = monitoringDeviceOptional.get();
            if (monitoringDevice.getIdPerson().equals(idOwner)) {
                return monitoringDevice.getTimestamp();
            }
        }
        return null;
    }

    public UUID getOwner(UUID uuid)
    { UUID owner;
        Optional<MonitoringDevice> monitoringDevice = monitoringDeviceRepository.findById(uuid);
        if(monitoringDevice.isPresent())
        {
             owner = monitoringDevice.get().getIdPerson();
            return owner;

        }
        return  null;

    }

    public Date getDate(UUID uuid)
    { Date date;
        Optional<MonitoringDevice> monitoringDevice = monitoringDeviceRepository.findById(uuid);
        if(monitoringDevice.isPresent())
        {
            date = monitoringDevice.get().getTimestamp();
            return date;

        }
        return  null;

    }

}
