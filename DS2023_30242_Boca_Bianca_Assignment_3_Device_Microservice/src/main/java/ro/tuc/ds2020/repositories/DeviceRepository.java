package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByDescription(String description);
    Device findDeviceByDescription(String description);
    Device findDeviceById(UUID ID);

    List<Device> findAllById(UUID uuid);
    List<Device> findAllByOwner(PersonTabel personTabel);
    @Query(value = "SELECT p " +
            "FROM Device p " +
            "WHERE p.description = :description " +
            "AND p.maxHours >= 60  ")
    Optional<Device> findSeniorsByName(@Param("description") String description);

}
