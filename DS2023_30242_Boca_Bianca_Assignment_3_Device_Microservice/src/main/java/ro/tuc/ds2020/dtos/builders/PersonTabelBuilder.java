package ro.tuc.ds2020.dtos.builders;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.PersonTabelDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;


public class PersonTabelBuilder {


    public static PersonTabelDTO toPersonDTO(PersonTabel person) {
        return new PersonTabelDTO(person.getId());
    }


    public static PersonDetailsDTO toPersonDetailsDTO(PersonTabel person) {
        return new PersonDetailsDTO(person);
    }


    public static PersonTabelDTO toEntity(PersonDetailsDTO personDetailsDTO) {
        return new PersonTabelDTO(
                personDetailsDTO.getId().getId());
    }
}

