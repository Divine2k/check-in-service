package com.event.check_in_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckInServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckInServiceApplication.class, args);
	}

}
