package com.example.devotionals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevotionalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevotionalsApplication.class, args);
	}

}
