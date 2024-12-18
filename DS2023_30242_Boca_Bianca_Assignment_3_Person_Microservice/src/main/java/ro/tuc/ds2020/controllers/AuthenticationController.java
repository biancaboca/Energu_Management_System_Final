package ro.tuc.ds2020.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.auth.AuthenticationRequest;
import ro.tuc.ds2020.dtos.FrontBodyLogin;
import ro.tuc.ds2020.services.AuthenticationService;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @Autowired
    private final PersonService personService;

    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @PostMapping("/register")
    public ResponseEntity<UUID> register(
            @RequestBody PersonDetailsDTO request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<FrontBodyLogin> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            UUID personID = service.authenticate(request);
            String authToken = service.sendingTokenToFront();
            FrontBodyLogin frontBodyLogin = new FrontBodyLogin(personID, authToken);

            // Optionally, log the successful authentication
            System.out.println("Authenticated person ID: " + personID + " with token: " + authToken);
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", authToken);
            UUID userAdmin = personService.findByRole("admin");


            HttpEntity<UUID> requestEntity = new HttpEntity<>(userAdmin, headers);

            logger.info("Sending UUID: " + userAdmin.toString());

//            ResponseEntity<UUID> responseEntity3 = restTemplate.postForEntity(
//                    "http://monitoring:8080/admin/hasAllPermision/add",
//                    userAdmin,
//                    UUID.class
//            );
//            ;ResponseEntity<UUID> responseEntity3 = restTemplate.postForEntity(
//                    "http://localhost:8032/admin/hasAllPermision/add/"+userAdmin,
//                    userAdmin,
//                    UUID.class
//            );
            return ResponseEntity.ok(frontBodyLogin);
        } catch (Exception e) {
            // Log exception and return an appropriate HTTP status
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
