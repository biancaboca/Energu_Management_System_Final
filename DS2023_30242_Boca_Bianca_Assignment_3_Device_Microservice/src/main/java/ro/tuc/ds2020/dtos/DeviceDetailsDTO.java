package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;
import ro.tuc.ds2020.entities.PersonTabel;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeviceDetailsDTO {


    private UUID id;
    private String name;
    private String address;
    private int maxHours;


//    public DeviceDetailsDTO(String name, String address, int maxHours, PersonTabel id) {
//        this.name = name;
//        this.address = address;
//        this.maxHours = maxHours;
//        this.id  =id;
//    }
    public DeviceDetailsDTO(String name, String address, int maxHours) {
        this.name = name;
        this.address = address;
        this.maxHours = maxHours;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return maxHours == that.maxHours &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, maxHours);
    }
}
