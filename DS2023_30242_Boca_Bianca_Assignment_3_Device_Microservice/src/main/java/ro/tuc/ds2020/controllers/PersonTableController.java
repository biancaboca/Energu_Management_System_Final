package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.PersonTabelDTO;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.repositories.PersonTableRepository;
import ro.tuc.ds2020.services.PersonTableService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/personInDevice")
public class PersonTableController {
    private final PersonTableService personTableService;

    @Autowired
    public PersonTableController(PersonTableService personTableService) {
        this.personTableService = personTableService;
    }

    @PostMapping("/hasAllPermision/personToDevice")
    public ResponseEntity<UUID> insertDeviceForPerson(@RequestBody UUID personID) {
//        PersonDetailsDTO personId = new PersonDetailsDTO();
//        PersonTabel personTabel=personTableService.findPerson(personID);
//        personId.setId(personTabel);
        UUID deviceID = personTableService.insertFromPersonId(personID); // Implement this method

        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @DeleteMapping("/hasAllPermision/personToDeviceDelete/{id}")
    public ResponseEntity<Void> deleteDeviceFromPerson(@PathVariable UUID id) {

       personTableService.deleteFromPersonId(id); // Implement this method

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/hasAllPermision/{id}")
    public ResponseEntity<PersonTabelDTO> getDevice(@PathVariable("id") UUID personId) {
        PersonTabelDTO dto = personTableService.findPersonOptional(personId);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
