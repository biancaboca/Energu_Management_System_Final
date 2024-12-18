package com.monitoring.comunication.controller;

import com.monitoring.comunication.Service.MonotoringService;
import com.monitoring.comunication.implementation.AdminImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    public AdminImplementation adminImplementation;


    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @PostMapping("/hasAllPermision/add/{personID}")
    public ResponseEntity<UUID> insertDeviceForPerson(@PathVariable("personID") UUID personID) {
        try {
            UUID adminId = adminImplementation.addAdmin(personID);
            System.out.println("adminId" +adminId);
            return new ResponseEntity<>(adminId, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inserting device for person", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
