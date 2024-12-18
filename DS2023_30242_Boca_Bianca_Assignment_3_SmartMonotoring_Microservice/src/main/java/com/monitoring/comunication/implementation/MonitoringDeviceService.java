package com.monitoring.comunication.implementation;

import com.monitoring.comunication.entity.MonitoringDevice;

import java.util.List;
import java.util.UUID;

public interface MonitoringDeviceService {


    MonitoringDevice updateMonitoringDevice(UUID id, MonitoringDevice device);

    MonitoringDevice findBYidDevice(UUID uuid);

    void createMonitoringDeviceRabbit(MonitoringDevice monitoringDevice);

    MonitoringDevice getMonitoringDeviceById(UUID id);
    List<MonitoringDevice> getAllMonitoringDevices();
    void deleteMonitoringDevice(UUID id);
    void updateList(UUID id, List<Double> list);
    MonitoringDevice updateListMeasure(UUID id, MonitoringDevice device);

    void getMeanForCurrentDevice(UUID deviceId, double mean, double maxValue);
}
