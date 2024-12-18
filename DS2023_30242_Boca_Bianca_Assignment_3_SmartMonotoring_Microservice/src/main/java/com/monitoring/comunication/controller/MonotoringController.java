package com.monitoring.comunication.controller;

import com.monitoring.comunication.Service.MonotoringService;
import com.monitoring.comunication.entity.MonitoringDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/monotoring")
public class MonotoringController {
    public MonotoringService monotoringService;


    @Autowired
    public  MonotoringController(MonotoringService monotoringService)
    {
        this.monotoringService=monotoringService;
    }

    @GetMapping("/hasAllPermision/getAll/{id}")
    public ResponseEntity<List<Double>> getAllMeasurement(@PathVariable("id")UUID id)
    {
        List<Double> doubleList= monotoringService.getListMeasurement(id);
        return  new ResponseEntity<>(doubleList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/hasAllPermision/getAllDevicesMeasured")
    public ResponseEntity<List<MonitoringDevice>> getAllDevices()
    {
        List<MonitoringDevice> monitoringDevices= monotoringService.getListMonotoring();
        return  new ResponseEntity<>(monitoringDevices, HttpStatus.ACCEPTED);
    }

    @GetMapping("/hasAllPermision/getTimestamp/{idOwner}/{idDevice}")
    public ResponseEntity<Date> getDateAndOwner(@PathVariable("idOwner")UUID idOwner, @PathVariable("idDevice")UUID idDevice)
    {
        Date date = monotoringService.getDateAndOwnerOfDevice(idOwner,idDevice);
        return  new ResponseEntity<>(date, HttpStatus.OK);
    }
    @GetMapping("/hasAllPermision/getOwner/{idOwner}")
    public ResponseEntity<UUID> getOwner(@PathVariable("idOwner")UUID idOwner)
    {
        UUID date = monotoringService.getOwner(idOwner);
        return  new ResponseEntity<>(date, HttpStatus.OK);
    }
    @GetMapping("/hasAllPermision/getDate/{idDevice}")
    public ResponseEntity<Date> getDate(@PathVariable("idDevice")UUID idDevice)
    {
        Date date = monotoringService.getDate(idDevice);
        return  new ResponseEntity<>(date, HttpStatus.OK);
    }


}
