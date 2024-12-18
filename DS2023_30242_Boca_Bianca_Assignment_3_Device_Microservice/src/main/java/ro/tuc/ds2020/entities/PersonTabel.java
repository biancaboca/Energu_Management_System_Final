package ro.tuc.ds2020.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonTabel {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="IdPerson")

    @Type(type = "uuid-binary")
    private UUID id;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> deviceList=new ArrayList<>();

    public PersonTabel(UUID id)
    {
        this.id=id;
    }


}


