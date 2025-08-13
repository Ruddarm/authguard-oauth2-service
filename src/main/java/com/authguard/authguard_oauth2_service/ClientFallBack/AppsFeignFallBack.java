package com.authguard.authguard_oauth2_service.ClientFallBack;

import java.util.UUID;

import com.authguard.authguard_oauth2_service.FeignClient.AppsFeignClient;
import com.authguard.authguard_oauth2_service.dtos.AppResponse;

public class AppsFeignFallBack implements AppsFeignClient {

    @Override
    public AppResponse getApp(UUID client_id) {
        return null;
    }

}
