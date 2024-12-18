package com.monitoring.comunication.implementation;

import com.monitoring.comunication.entity.MonitoringDevice;
import com.monitoring.comunication.repository.MonitoringDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class MonitoringDeviceServiceImpl implements MonitoringDeviceService {

  private final MonitoringDeviceRepository repository;

  @Autowired
  public MonitoringDeviceServiceImpl(MonitoringDeviceRepository repository) {
    this.repository = repository;
  }


  @Override
  public MonitoringDevice updateMonitoringDevice(UUID id, MonitoringDevice device) {
    return repository.findById(id)
            .map(existingDevice -> {
              existingDevice.setIdPerson(device.getIdPerson());
              existingDevice.setMaxHours(device.getMaxHours());
              existingDevice.setMedieAritmetica(device.getMedieAritmetica());
              existingDevice.setTimestamp(new Date());
              existingDevice.setAllMeasuringMeters(device.getAllMeasuringMeters());
              return repository.save(existingDevice);
            }).orElse(null);
  }

  @Override
  public MonitoringDevice findBYidDevice(UUID uuid)
  {
    return repository.findById(uuid).get();
  }

  @Override
  public void createMonitoringDeviceRabbit(MonitoringDevice monitoringDevice) {
    repository.save(monitoringDevice);
  }

  @Override
  public MonitoringDevice getMonitoringDeviceById(UUID id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<MonitoringDevice> getAllMonitoringDevices() {
    return repository.findAll();
  }

  @Override
  public void deleteMonitoringDevice(UUID id) {
    MonitoringDevice monitoringDevice = repository.findById(id).get();
    repository.delete(monitoringDevice);
  }

  @Override
  public void updateList(UUID id, List<Double> list) {
    repository.findById(id)
            .ifPresent(device -> {
              device.setAllMeasuringMeters(list);
              repository.save(device);
            });
  }

  @Override
  public MonitoringDevice updateListMeasure(UUID id, MonitoringDevice device) {
    Optional<MonitoringDevice> existingDeviceOpt = repository.findById(id);
    if (existingDeviceOpt.isPresent()) {
      MonitoringDevice existingDevice = existingDeviceOpt.get();
      List<Double> updatedMeasurements = device.getAllMeasuringMeters();
      existingDevice.setAllMeasuringMeters(updatedMeasurements);
      return repository.save(existingDevice);
    }
    return null;
  }

  @Override
  public void getMeanForCurrentDevice(UUID deviceId, double mean, double maxValue) {
    Optional<MonitoringDevice> monitoringDeviceOptional = repository.findById(deviceId);

    if (monitoringDeviceOptional.isPresent()) {
      MonitoringDevice monitoringDevice = monitoringDeviceOptional.get();

      if (mean > maxValue) {
        String message = String.format("Threshold exceeded for device %s: Mean value %f is greater than max value %f",
                deviceId, mean, maxValue);
    //    template.convertAndSend("/topic/notification", message);
      }
    } else {
      // Handle the case where the device is not found in the repository
      // For example, log an error or throw an exception
      System.out.println("Device not found with ID: " + deviceId);
    }
  }
}
