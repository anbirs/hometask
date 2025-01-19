package com.example.hometask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(exclude = {org.springdoc.core.SpringDocConfiguration.class})
public class HometaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HometaskApplication.class, args);
	}

}
