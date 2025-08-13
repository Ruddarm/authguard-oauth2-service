package com.authguard.authguard_oauth2_service.FeignClient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;

@FeignClient(name = "authguard-app-service")
public interface AppsFeignClient {
    
    @GetMapping("/service/apps/app/{client_id}")
    String getApp(@PathVariable("client_id") UUID client_id);

}
