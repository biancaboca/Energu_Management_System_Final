package com.monitoring.comunication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class ComunicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComunicationApplication.class, args);
	}

}
