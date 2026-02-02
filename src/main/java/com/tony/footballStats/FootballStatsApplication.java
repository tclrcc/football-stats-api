package com.tony.footballStats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootballStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballStatsApplication.class, args);
	}

}
