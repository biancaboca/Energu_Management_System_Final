package ro.tuc.ds2020.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.JsonDeleteFormat;
import ro.tuc.ds2020.dtos.jsonSendingFormat;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.PersonTableService;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
    static String QUEUE_NAME = "stringDevice";


    private final DeviceService deviceService;
    private final PersonTableService personTableService;

    private final RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    String queueNameInsert = "insertDevice";
    String queueNameDelete = "deleteDevice";
    String queueNameUpdate = "updateDevice";

    @Autowired
    public DeviceController(DeviceService deviceService, PersonTableService personTableService, RabbitTemplate rabbitTemplate1, ObjectMapper objectMapper) {
        this.deviceService = deviceService;
        this.personTableService = personTableService;
        this.rabbitTemplate = rabbitTemplate1;
        this.objectMapper = objectMapper;

    }




    @GetMapping(value = "/hasAllPermision/getAll/{personId}")
    public ResponseEntity<List<DeviceDTO>> getDevices(@PathVariable(name = "personId") UUID personId) {
        List<DeviceDTO> dtos = deviceService.findDevicesForPerson(personId);
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/hasAdminPermision/{id}")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDto, @PathVariable("id") UUID id) throws JsonProcessingException {
        UUID deviceId = deviceService.insert(id,deviceDto);
        jsonSendingFormat data = new jsonSendingFormat();
        data.setIdDevice(deviceId);
        data.setIdUser(id);
        data.setMaxHours(deviceDto.getMaxHours());
        System.out.println(id);
        System.out.println("device"+deviceId);

        String json = data.toJson();
        System.out.println(json);
        String queuePayloadString = objectMapper.writeValueAsString(deviceId);
        rabbitTemplate.convertAndSend(queueNameInsert, json);

        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/hasAdminPermision/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID personId) {
        DeviceDetailsDTO dto = deviceService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/hasAllPermision/getAllDevices/{id}")
    public ResponseEntity<List<Device>> getDeviceListOfUser(@PathVariable("id") UUID personId) {

        List<Device> deviceList = deviceService.getAllDevicesForUser(personId);
        return new ResponseEntity<>(deviceList, HttpStatus.OK);
    }

    @PostMapping(value = "/hasAdminPermision/{id}/{address}/{description}/{maxHours}")
    public ResponseEntity<Device> updateDevie(@PathVariable("id") UUID id, @PathVariable("address") String address, @PathVariable("description") String description, @PathVariable("maxHours") int MaxHours)
    {
        Device device = deviceService.updateDevice(id, address, description, MaxHours);

        jsonSendingFormat data = new jsonSendingFormat();
        data.setIdDevice(id);
        data.setIdUser(device.getOwner().getId());
        data.setMaxHours(MaxHours);

        System.out.println(device.getOwner().getId());
        System.out.println("device"+id);

        String json = data.toJson();
        rabbitTemplate.convertAndSend(queueNameUpdate, json);

        System.out.println(json);

        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @DeleteMapping(value="/hasAdminPermision/{id}")
    public ResponseEntity<Map<String,String>> deleteDevice(@PathVariable("id") UUID id)
    {
        deviceService.deleteDevice(id);
    Map<String,String> succesReturn = new HashMap<>();
    succesReturn.put("status","success");

        //        JsonDeleteFormat data = new JsonDeleteFormat(id);
//
//        System.out.println("device"+id);
//        String json = data.toJsonDelete();
//        rabbitTemplate.convertAndSend(queueNameDelete , json);
        return new ResponseEntity<>(succesReturn,HttpStatus.OK);
    }



    @PostMapping("/hasAdminPermision/device")
    public ResponseEntity<UUID> insertDeviceForPerson(@RequestBody UUID personID) {
        // Create a new Device associated with the provided Person's ID
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO();
        PersonTabel personTabel = personTableService.findPerson(personID);


        UUID deviceID = deviceService.insertFromPerson(deviceDTO); // Implement this method

        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping("/hasAllPermision/api/resource")
    public ResponseEntity<String> getResource() {
        // Extract user roles and username from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            // Handle the case where there is no authentication information
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication information is missing.");
        }

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Construct the response based on the user and their roles
        String response = "Resource accessed by: " + username + " with roles: " + authorities;

        System.out.println("auth"+authorities);
        return ResponseEntity.ok(response);
    }

}
