package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.dtos.validators.annotation.RoleLimit;

import java.util.Objects;
import java.util.UUID;
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO extends RepresentationModel<UserDTO>  {
    private UUID id;
    private String name;
    @RoleLimit
    private String role;
    private String username;
    private String password;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO personDTO = (UserDTO) o;
        return role.equals(personDTO.role)  &&
                Objects.equals(name, personDTO.name) &&
                Objects.equals(username, personDTO.username) &&
                Objects.equals(password, personDTO.password );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, username, password);
    }
}
