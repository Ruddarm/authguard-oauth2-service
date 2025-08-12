package com.authguard.authguard_oauth2_service.FeignClient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;

@FeignClient(name = "authguard-app-service")
public interface AppsFeignClient {
    @Headers("X-USER-Id: 123e4567-e89b-12d3-a456-426614174000")
    @GetMapping("/apps/app/")
    String getApp(@RequestParam("client_id") UUID client_id);

}
