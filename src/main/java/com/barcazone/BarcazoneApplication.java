package com.barcazone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BarcazoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarcazoneApplication.class, args);
	}

}
