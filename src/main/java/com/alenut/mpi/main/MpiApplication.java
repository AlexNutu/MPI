package com.alenut.mpi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@ComponentScan({"com.alenut.mpi.models", "com.alenut.mpi.main", "controller",
		"com.alenut.mpi.service", "com.alenut.mpi.repository",
		"com.alenut.mpi.auxiliary"})
@EnableJpaRepositories("com.alenut.mpi.repository")
@EntityScan(basePackages = {"com.alenut.mpi.models"})
@EnableScheduling
@Configuration
public class MpiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpiApplication.class, args);
	}
}
