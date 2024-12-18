package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;

import java.util.List;
import java.util.UUID;

public interface PersonTableRepository extends JpaRepository<PersonTabel, UUID> {

    PersonTabel findPersonTabelById(UUID uuid);
    List<Device> findAllByDeviceList(UUID id);
}
