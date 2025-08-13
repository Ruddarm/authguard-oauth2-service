package com.authguard.authguard_oauth2_service.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.authguard.authguard_oauth2_service.Context.AccessToken;
import com.authguard.authguard_oauth2_service.Exception.ResourceException;
import com.authguard.authguard_oauth2_service.FeignClient.AppsFeignClient;
import com.authguard.authguard_oauth2_service.FeignClient.UserFeignClient;
import com.authguard.authguard_oauth2_service.dtos.AppResponse;
import com.authguard.authguard_oauth2_service.model.AuthorizeCodePayload;
import com.authguard.authguard_oauth2_service.model.User;

import io.jsonwebtoken.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final RedisService redisService;
    private final CacheManager cacheManager;
    private final AppsFeignClient appsFeignClient;
    private final UserFeignClient userFeignClient;
    // private final CacheUtils cacheUtils;
    private final JwtService jwtService;

    public String[] validateCode(String client_id, String code, String client_secret)
            throws InvalidKeyException, ResourceException, Exception {
        AuthorizeCodePayload authCodePayload = redisService.getFromCache("AuthorizeCode::" + code,
                AuthorizeCodePayload.class);
        if (authCodePayload == null) {
            // if code is not found in cache
            throw new ResourceException("Code is invalid or expired");
        }
        if (!authCodePayload.getClient_Id().equals(client_id))
            throw new ResourceException("Client  Id did not matched");
        Map claimsMap = Map.of("aud", "authgurad-app-service");
        String ServiceaccessToken = "Bearer " + jwtService.serviceAccessToken(claimsMap);
        AccessToken.set(ServiceaccessToken);
        AppResponse app = appsFeignClient.getApp(UUID.fromString(client_id));
        if (app == null) {
            throw new ResourceException("App not found with " + client_id);
        }
        AccessToken.clear(); // clear context to avoid memory leek
        if (!app.getClient_secret().equals(client_secret)) {
            /* Throw invlaid clent secret if it does not match */
            throw new ResourceException("Invalid Client Secret");
        }
        /*
         * Fetching user detials from user-service
         */
        claimsMap = Map.of("aud", "authgurad-user-service");
        ServiceaccessToken = "Bearer " + jwtService.serviceAccessToken(claimsMap);
        AccessToken.set(ServiceaccessToken);
        User user = userFeignClient.getUserbyId(UUID.fromString(authCodePayload.getUserId()));
        AccessToken.clear();
        if (user == null)
            throw new ResourceException("User not found with " + authCodePayload.getUserId());
        // Todo : User - app link implmentation
        String accessToken = jwtService.createOauth2AccessToken(authCodePayload.getUserId(), client_id);
        // String refreshToken =
        // jwtService.createreOauth2refreshToken(authCodePayload.getUserId(),
        // client_id);
        String idToken = jwtService.createIdToken(authCodePayload.getNonce(), user,
                authCodePayload.getClient_Id());
        return new String[] { accessToken, idToken };

    }
}
