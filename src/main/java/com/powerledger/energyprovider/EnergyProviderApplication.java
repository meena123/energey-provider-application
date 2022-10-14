package com.powerledger.energyprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class EnergyProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyProviderApplication.class, args);
	}
	@Bean
	public SpringApplicationContext SpringApplicationContext()
	{
		return  new SpringApplicationContext();
	}
}
