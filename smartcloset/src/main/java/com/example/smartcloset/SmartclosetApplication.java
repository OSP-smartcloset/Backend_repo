package com.example.smartcloset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartclosetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartclosetApplication.class, args);
	}
}
