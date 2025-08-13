package com.authguard.authguard_oauth2_service.FeignClient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.authguard.authguard_oauth2_service.ClientFallBack.AppsFeignFallBack;
import com.authguard.authguard_oauth2_service.dtos.AppResponse;

import feign.Headers;

@FeignClient(name = "authguard-app-service", fallback = AppsFeignFallBack.class)
public interface AppsFeignClient {

    @GetMapping("/service/apps/app/{client_id}")
    AppResponse getApp(@PathVariable("client_id") UUID client_id);

}
