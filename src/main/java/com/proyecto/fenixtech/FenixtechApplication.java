package com.proyecto.fenixtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FenixtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(FenixtechApplication.class, args);
	}
	
}
