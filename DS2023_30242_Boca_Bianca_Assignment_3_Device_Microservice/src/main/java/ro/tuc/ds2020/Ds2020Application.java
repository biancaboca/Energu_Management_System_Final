package ro.tuc.ds2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.repositories.PersonTableRepository;

import java.util.TimeZone;
import java.util.UUID;

@SpringBootApplication
@Validated
@EnableJpaRepositories
@ComponentScan("ro.tuc.ds2020.auth")
@ComponentScan("ro.tuc.ds2020.controllers")
@ComponentScan("ro.tuc.ds2020.services")
@ComponentScan("ro.tuc.ds2020.repositories")
public class Ds2020Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

    public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Ds2020Application.class, args);

    }
}
