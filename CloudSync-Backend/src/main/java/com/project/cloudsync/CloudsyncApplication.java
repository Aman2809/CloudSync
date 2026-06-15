package com.project.cloudsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CloudsyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudsyncApplication.class, args);
	}

}
