package com.newtrendz.pass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewTrendzPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewTrendzPassApplication.class, args);
	}

}
