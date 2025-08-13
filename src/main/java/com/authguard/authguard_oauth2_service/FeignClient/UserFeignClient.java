package com.authguard.authguard_oauth2_service.FeignClient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.authguard.authguard_oauth2_service.ClientFallBack.UserFeignFallBack;
import com.authguard.authguard_oauth2_service.model.User;

@FeignClient(name = "authguard-user-service", path = "/service/user", fallback = UserFeignFallBack.class)
public interface UserFeignClient {

    @GetMapping("/{userId}")
    public User getUserbyId(@PathVariable UUID userId);
}
