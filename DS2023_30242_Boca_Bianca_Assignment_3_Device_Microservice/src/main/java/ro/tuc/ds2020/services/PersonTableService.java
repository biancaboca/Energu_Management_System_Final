package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.PersonTabelDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.PersonTabelBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.repositories.PersonTableRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PersonTableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final PersonTableRepository personTableRepository;

    @Autowired
    public PersonTableService(PersonTableRepository personTableRepository) {
        this.personTableRepository = personTableRepository;
    }

    public PersonTabel insertPersonDB(UUID uuid)
    {
        PersonTabel personTabel = new PersonTabel();
        personTabel.setId(uuid);
        personTableRepository.save(personTabel);
        return personTabel;
    }

    public void deletePersonDB(UUID uuid)
    {
        PersonTabel personTabel = personTableRepository.findPersonTabelById(uuid);
        personTableRepository.delete(personTabel);
    }

    public UUID insertFromPerson(PersonDetailsDTO deviceDTO) {
        PersonTabel device = new PersonTabel();
        device.setId(deviceDTO.getId2());
        personTableRepository.save(device);
        return device.getId();
    }

    public UUID insertFromPersonId(UUID id) {
        PersonTabel personTabel = new PersonTabel();
        personTabel.setId(id);
        personTableRepository.save(personTabel);
        return id;
    }
    public void deleteFromPersonId(UUID id) {
        PersonTabel personTabel = personTableRepository.findById(id).get();
        personTableRepository.delete(personTabel);
    }




    public PersonTabel findPerson(UUID uid)
    {
        return personTableRepository.findPersonTabelById(uid);
    }

    public PersonTabelDTO findPersonOptional(UUID id) {
        Optional<PersonTabel> prosumerOptional = personTableRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return PersonTabelBuilder.toPersonDTO(prosumerOptional.get());
    }

    public boolean findPersonOptionalBoolean(UUID id) {
        Optional<PersonTabel> prosumerOptional = personTableRepository.findById(id);
        if (!prosumerOptional.isPresent())
        {
            LOGGER.error("Person with id {} was not found in db", id);
            return false;
        }
        return true;
    }

    public boolean connected(UUID id)
    {
        List<PersonTabel> personTabels = personTableRepository.findAll();
        for (PersonTabel person: personTabels) {
            if(person.getId()==id)
                return true;
            else
                return  false;

        }
        return false;
    }


}

