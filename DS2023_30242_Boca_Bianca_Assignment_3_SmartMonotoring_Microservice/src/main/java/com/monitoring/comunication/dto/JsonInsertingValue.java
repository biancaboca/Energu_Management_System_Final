package com.monitoring.comunication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class JsonInsertingValue {
    private UUID idDevice;
    private UUID idUser;
    private double maxHours;
}
