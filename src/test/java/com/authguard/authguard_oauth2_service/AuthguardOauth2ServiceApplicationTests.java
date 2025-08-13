package com.authguard.authguard_oauth2_service;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.authguard.authguard_oauth2_service.Context.AccessToken;
import com.authguard.authguard_oauth2_service.FeignClient.AppsFeignClient;
import com.authguard.authguard_oauth2_service.dtos.AppResponse;
import com.authguard.authguard_oauth2_service.services.JwtService;

import io.jsonwebtoken.security.InvalidKeyException;

@SpringBootTest
class AuthguardOauth2ServiceApplicationTests {

	@Autowired
	private AppsFeignClient appFeingClient;

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
	}

	@Test
	void getApp() throws InvalidKeyException, Exception {
		Map claimsMap = Map.of("aud", "oauth-service");
		String accessToken = "Bearer " + jwtService.serviceAccessToken(claimsMap);
		AccessToken.set(accessToken);
		AppResponse app = appFeingClient.getApp(UUID.fromString("1d4b3eb7-0dbd-4efb-887e-c65c47ba0cee"));
		System.out.println(app);
	}

}
