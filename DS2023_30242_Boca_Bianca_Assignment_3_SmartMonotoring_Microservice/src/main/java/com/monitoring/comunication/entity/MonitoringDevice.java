package com.monitoring.comunication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDevice {
    @Id
    private UUID idDevice;
    private UUID idPerson;
    private double maxHours;
    private double medieAritmetica;
    private Date timestamp;


    @ElementCollection(fetch = FetchType.EAGER)

    private List<Double> allMeasuringMeters;

}
