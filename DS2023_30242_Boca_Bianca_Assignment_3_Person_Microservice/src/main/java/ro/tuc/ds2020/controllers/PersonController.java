package ro.tuc.ds2020.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.auth.AuthenticationRequest;
import ro.tuc.ds2020.dtos.FrontBodyLogin;
import ro.tuc.ds2020.dtos.PersonRegistrationResponse;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.AuthenticationService;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    public PersonController(PersonService personService, AuthenticationService authenticationService) {
        this.authenticationService=authenticationService;
        this.personService = personService;
    }


    @GetMapping("/hasAdminPermision/getAll")
    public ResponseEntity<List<UserDTO>> getPersons() {
        List<UserDTO> dtos = personService.findPersons();
        for (UserDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

/*    @PostMapping()
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody PersonDetailsDTO UserDTO) {
        UUID personID = personService.insert(UserDTO);
        ResponseEntity<UUID> responseEntity = restTemplate.postForEntity("http://localhost:8081/personInDevice/personToDevice", personID, UUID.class);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }*/
@PostMapping("/hasAdminPermision")
public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody PersonDetailsDTO userDTO) throws JsonProcessingException {
    UUID personId = authenticationService.register(userDTO);

    UUID userAdmin = personService.findByRole("admin");

    PersonRegistrationResponse response = new PersonRegistrationResponse(personId, userDTO.getRole());

    // Log the request for debugging
    ObjectMapper mapper = new ObjectMapper();
    String jsonRequest = mapper.writeValueAsString(response);
    System.out.println("Sending JSON: " + jsonRequest);

    ResponseEntity<UUID> responseEntity = restTemplate.postForEntity(
            "http://springdevice:8080/personInDevice/hasAllPermision/personToDevice",
            personId,
            UUID.class
    );

    ResponseEntity<UUID> responseEntity2 = restTemplate.postForEntity(
            "http://springdevice:8080/personInDevice/hasAllPermision/personToDevice",
            userAdmin,
            UUID.class
    );

    ResponseEntity<UUID> responseEntity3 = restTemplate.postForEntity(
            "http://monitoring:8080/admin/hasAllPermision/add/" + userAdmin,
            null,
            UUID.class
    );



    return new ResponseEntity<>(personId, HttpStatus.CREATED);
}


    @GetMapping(value = "/hasAdminPermision/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }



    @PostMapping(value = "/hasAdminPermision/{personId}/{name}/{username}/{password}")
    public ResponseEntity<PersonDetailsDTO> updateName(
            @PathVariable("personId") UUID personId,
            @PathVariable("name") String name,
            @PathVariable("username") String username,
            @PathVariable("password") String password)
        {
        //PersonDetailsDTO dto = personService.findPersonById(personId);
        PersonDetailsDTO dto =personService.updateNameUsernamePassword(personId, name,username,password);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @DeleteMapping(value = "/hasAdminPermision/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") UUID personId) {
        // Check if the person exists before attempting to delete
        PersonDetailsDTO user = personService.findPersonById(personId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Proceed with deletion
        personService.deletePersonById(personId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(personId.toString(), headers);


        String deleteUrl = String.format("http://springdevice:8080/personInDevice/hasAllPermision/personToDeviceDelete/%s", personId);
        restTemplate.delete(deleteUrl);


        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(value = "/hasAllPermision/{username}/{password}")
    public ResponseEntity<FrontBodyLogin> login(@PathVariable("username") String username, @PathVariable("password") String password)
    {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username,password);
        UUID prsonID = authenticationService.authenticate(authenticationRequest);
        String authToken = authenticationService.sendingTokenToFront();

        FrontBodyLogin frontBodyLogin = new FrontBodyLogin(prsonID,authToken);
        try {
            String response = PersonService.sendHttpRequestWithToken("http://sdb_tema3-springdevice-1:8080/device/api/resource", "GET", authToken);
            System.out.println("response: " + response);
        } catch (IOException e) {
            System.out.println("An IOException occurred during the HTTP request:");
            e.printStackTrace();
        }

        return new ResponseEntity<>(frontBodyLogin,HttpStatus.OK);
    }


    @GetMapping(value = "/hasAllPermision/verifying/{id}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("id") UUID personId) {
        Boolean isUser = personService.verifyingUser(personId);
        return new ResponseEntity<>(isUser, HttpStatus.OK);
    }



}
