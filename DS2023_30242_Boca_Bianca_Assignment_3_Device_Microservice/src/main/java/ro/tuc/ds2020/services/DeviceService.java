package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.PersonTableRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final PersonTableRepository personTableRepository;
    private final PersonTableService personTableService;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonTableRepository personTableRepository, PersonTableService personTableService) {
        this.deviceRepository = deviceRepository;
        this.personTableRepository = personTableRepository;
        this.personTableService = personTableService;
    }

//    public List<DeviceDTO> findDevice(UUID id) {
//        boolean adevaratSauFals = personTableService.findPersonOptionalBoolean(id) ;
//        if (!adevaratSauFals) {
//            LOGGER.debug("There no exist a person with this id");
//            return  null;
//        } else {
//            List<Device> personList = deviceRepository.findAll();
//            return personList.stream()
//                    .map(DeviceBuilder::toPersonDTO)
//                    .collect(Collectors.toList());
//        }
//    }
public List<DeviceDTO> findDevicesForPerson(UUID personId) {
    boolean personExists = personTableService.findPersonOptionalBoolean(personId);
    if (!personExists) {
        LOGGER.debug("There is no person with this id");
        return new ArrayList<>(); // Return an empty list
    } else {
        PersonTabel personTabel = personTableRepository.findPersonTabelById(personId);
        List<Device> devicesForPerson = deviceRepository.findAllByOwner(personTabel);

        return devicesForPerson.stream()
                .map(DeviceBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }
}


    public List<Device> getAllDevicesForUser(UUID userId) {
        Optional<PersonTabel> user = personTableRepository.findById(userId);

        if (user.isPresent()) {
            return user.get().getDeviceList();
        } else {
            return Collections.emptyList(); // Return an empty list if the user is not found
        }
    }

    public DeviceDetailsDTO findPersonById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

//    public UUID insert(UUID id,DeviceDetailsDTO deviceDTO) {
//
//        boolean adevaratSauFals = personTableService.findPersonOptionalBoolean(id) ;
//
//        if (!adevaratSauFals) {
//            return  null;
//        } else {
//            Device device = DeviceBuilder.toEntity(deviceDTO);
//            PersonTabel personTabel1 = personTableRepository.findPersonTabelById(id);
//            device.setOwner(personTabel1);
//            device = deviceRepository.save(device);
//            List<Device> deviceListPerson= personTabel1.getDeviceList();
//            deviceListPerson.add(device);
//            personTabel1.setDeviceList(deviceListPerson);
//
//            for (Device device1: personTabel1.getDeviceList()) {
//                System.out.println(device1);
//
//            }
//            return device.getOwner().getId();
//        }
//    }
@Transactional
public UUID insert(UUID id, DeviceDetailsDTO deviceDTO) {
    boolean isValidPerson = personTableService.findPersonOptionalBoolean(id);

    if (!isValidPerson) {
        return null;
    } else {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        PersonTabel personTabel1 = personTableRepository.findPersonTabelById(id);
        device.setOwner(personTabel1);
        device = deviceRepository.save(device);
        List<Device> deviceListPerson = personTabel1.getDeviceList();
        deviceListPerson.size();
        deviceListPerson.add(device);
        personTabel1.setDeviceList(deviceListPerson);

        return device.getId();
    }
}

    public Device updateDevice(UUID id, String address,String description, int MaxHours)
    {

            Device device = deviceRepository.findById(id).get();
            device.setAddress(address);
            device.setMaxHours(MaxHours);
            device.setDescription(description);
            deviceRepository.save(device);
            return device;

    }

    public void deleteDevice(UUID id)
    {

            Device device = deviceRepository.findDeviceById(id);
            deviceRepository.delete(device);

    }

    public UUID insertFromPerson(DeviceDetailsDTO deviceDTO) {
        Device device = new Device();
        device.setOwner(personTableRepository.findPersonTabelById(device.getOwner().getId()));
        device.setAddress("null");
        device.setDescription("null");
        deviceRepository.save(device);
        return device.getOwner().getId();
    }


}
