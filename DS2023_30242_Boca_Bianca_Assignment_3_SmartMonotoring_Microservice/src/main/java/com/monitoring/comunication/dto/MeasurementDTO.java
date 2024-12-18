package com.monitoring.comunication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MeasurementDTO {
    private UUID deviceID;
    private String time;
    private Double value;

    // Constructor, getters, and setters
}
