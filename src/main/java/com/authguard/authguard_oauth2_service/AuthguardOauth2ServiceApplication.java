package com.authguard.authguard_oauth2_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthguardOauth2ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthguardOauth2ServiceApplication.class, args);
	}

}
