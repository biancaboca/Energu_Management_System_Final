package com.monitoring.comunication.Service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DevicepPayLoadDto {

    private UUID id;
    private double measurmentCode; // renamed from measurmentCode
    private Date timestamp;


    @JsonCreator
    public DevicepPayLoadDto(
            @JsonProperty("id") UUID id,
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("measurementCode") double measurmentCode) {

        this.id =id;
        this.measurmentCode = measurmentCode;
        this.timestamp = timestamp;
    }
 /*   @JsonCreator
    public DevicepPayLoadDto(
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("measurementCode") double measurmentCode) {

        this.measurmentCode = measurmentCode;
        this.timestamp = timestamp;
    }*/

}

