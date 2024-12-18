package com.monitoring.comunication.repository;


import com.monitoring.comunication.entity.MonitoringDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MonitoringDeviceRepository extends JpaRepository<MonitoringDevice, UUID> {
    // Standard CRUD operations are provided by JpaRepository
    // Add custom queries here if needed

   // MonitoringDevice findByIdDevice(UUID id);

    MonitoringDevice findByIdPerson (UUID id);
}
