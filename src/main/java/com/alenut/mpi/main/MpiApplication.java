package com.alenut.mpi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan({"com.alenut.mpi.config", "com.alenut.mpi.controllers","com.alenut.mpi.entities", "com.alenut.mpi.main",
        "com.alenut.mpi.service", "com.alenut.mpi.repository",
        "com.alenut.mpi.auxiliary",})
@EnableJpaRepositories("com.alenut.mpi.repository")
@EntityScan(basePackages = {"com.alenut.mpi.entities"})
@EnableScheduling
//@EnableOAuth2Sso tried for facebook login
//@Configuration
public class MpiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpiApplication.class, args);
    }

}
