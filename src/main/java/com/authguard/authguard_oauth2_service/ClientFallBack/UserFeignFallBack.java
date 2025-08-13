package com.authguard.authguard_oauth2_service.ClientFallBack;

import java.util.UUID;

import com.authguard.authguard_oauth2_service.FeignClient.UserFeignClient;
import com.authguard.authguard_oauth2_service.model.User;

public class UserFeignFallBack implements UserFeignClient {

    @Override
    public User getUserbyId(UUID userId) {

        return null;
    }

}
