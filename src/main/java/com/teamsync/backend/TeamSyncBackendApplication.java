package com.teamsync.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.teamsync.backend.repository")  // Replace with correct package
@EntityScan(basePackages = "com.teamsync.backend.model")  // Replace with correct package
public class TeamSyncBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamSyncBackendApplication.class, args);
	}

}
