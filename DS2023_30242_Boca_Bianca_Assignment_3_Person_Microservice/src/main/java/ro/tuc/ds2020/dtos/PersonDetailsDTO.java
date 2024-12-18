package ro.tuc.ds2020.dtos;

import lombok.*;
import ro.tuc.ds2020.dtos.validators.annotation.RoleLimit;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDetailsDTO {

    private UUID id;
    @NotNull
    private String name;
    @NotNull
    @RoleLimit
    private String role;

    private String password;
    private String username;

    public PersonDetailsDTO( String name, String role,String password, String username) {
        this.name = name;
        this.role = role;
        this.password = password;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return
                Objects.equals(name, that.name) &&
                Objects.equals(role, that.role) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, username, password);
    }
}
