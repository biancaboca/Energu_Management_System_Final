package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.User;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static UserDTO toPersonDTO(User person) {
        return new UserDTO(person.getId(), person.getName(), person.getRole(), person.getUsername(), person.getPassword());
    }

    public static PersonDetailsDTO toPersonDetailsDTO(User person) {
        return new PersonDetailsDTO(person.getId(), person.getName(), person.getRole(), person.getUsername(), person.getPassword());
    }

        public static User toEntity(PersonDetailsDTO personDetailsDTO) {
        return new User(personDetailsDTO.getName(),
                personDetailsDTO.getRole(),
                personDetailsDTO.getPassword(),
                personDetailsDTO.getUsername());
    }
}
