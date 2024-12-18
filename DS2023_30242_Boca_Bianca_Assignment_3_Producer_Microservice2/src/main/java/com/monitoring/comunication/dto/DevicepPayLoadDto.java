package com.monitoring.comunication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DevicepPayLoadDto {
    private UUID id;
    private Date timestamp;
    private double measurmentCode;

    public DevicepPayLoadDto(Date timestamp, double measurmentCode)
    {
        this.timestamp=timestamp;
        this.measurmentCode=measurmentCode;
    }

}
