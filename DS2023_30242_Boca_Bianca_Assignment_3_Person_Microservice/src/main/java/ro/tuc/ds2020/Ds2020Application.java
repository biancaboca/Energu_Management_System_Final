package ro.tuc.ds2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;

import java.util.TimeZone;
import java.util.UUID;

@SpringBootApplication
@Validated
@ServletComponentScan
@ComponentScan(value = "ro.tuc.ds2020.configuration")
@ComponentScan(value = "ro.tuc.ds2020.repositories")
public class Ds2020Application extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    PersonRepository personRepository ;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    RestTemplate restTemplate;
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

    public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Ds2020Application.class, args);


    }

    public void run(String... args) {
        User adminAcc = personRepository.findByRole("admin");
        if (adminAcc == null) {
            User person = new User();

            person.setName("eu");
            person.setUsername("eu");
            person.setRole("admin");
            person.setPassword(new BCryptPasswordEncoder().encode("eu"));

            // First save the person to generate the ID
            personRepository.save(person);

            // Now the person's ID should be generated and can be used
          //  restTemplate.postForEntity("http://localhost:8031/personInDevice/hasAllPermision/personToDevice", person.getId(), UUID.class);
        }
    }


}
