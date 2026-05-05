package com.smartnotes.smartnotes_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartnotesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartnotesBackendApplication.class, args);
	}

}
