package com.authguard.authguard_oauth2_service;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.authguard.authguard_oauth2_service.Context.AccessToken;
import com.authguard.authguard_oauth2_service.FeignClient.UserFeignClient;
import com.authguard.authguard_oauth2_service.model.User;
import com.authguard.authguard_oauth2_service.services.JwtService;

import io.jsonwebtoken.security.InvalidKeyException;

@SpringBootTest
public class UserFeigntest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserFeignClient userFeignClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void getUserTest() throws InvalidKeyException, Exception {
        Map claimsMap = Map.of("aud", "oauth-service");
        String accessToken = "Bearer " + jwtService.serviceAccessToken(claimsMap);
        AccessToken.set(accessToken);
        User user = userFeignClient.getUserbyId(UUID.fromString("de2f2fa5-281b-4a7f-affb-c6870ad1e8f9"));
        System.out.println(user);
    }
}
