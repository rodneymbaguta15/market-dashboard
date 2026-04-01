package com.market_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MarketDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketDashboardApplication.class, args);

        System.out.println("Market Dashboard Application started successfully!");
	}

}
