package com.authguard.authguard_oauth2_service;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.authguard.authguard_oauth2_service.FeignClient.AppsFeignClient;

@SpringBootTest
class AuthguardOauth2ServiceApplicationTests {

	@Autowired
	private AppsFeignClient appFeingClient;
	@Test
	void contextLoads() {
	}

	@Test
	void getApp(){	
		System.out.println(appFeingClient.getApp(UUID.randomUUID()));
	}


}
