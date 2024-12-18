package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Device  implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   // @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")

    private UUID id;

    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "Address", nullable = true)
    private String address;

    @Column(name = "MaxHours")
    private int maxHours;


    @ManyToOne
    @JoinColumn(name = "idPerson")
   // @Type(type = "uuid-binary")
    private PersonTabel owner;


    public Device(String name, String address, int maxHours, PersonTabel owner) {
        this.description = name;
        this.address = address;
        this.maxHours = maxHours;
        this.owner = owner;
    }
    public Device(String name, String address, int maxHours) {
        this.description = name;
        this.address = address;
        this.maxHours = maxHours;
    }

}
