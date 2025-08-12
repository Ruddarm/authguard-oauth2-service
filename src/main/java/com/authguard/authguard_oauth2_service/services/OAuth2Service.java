package com.authguard.authguard_oauth2_service.services;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.authguard.authguard_oauth2_service.Exception.ResourceException;
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
    // private final CacheUtils cacheUtils;
    private final JwtService jwtService;

    public String[] validateCode(String client_id, String code, String client_secret)
            throws InvalidKeyException, Exception {
        AuthorizeCodePayload authCodePayload = redisService.getFromCache("AuthorizeCode::" + code,
                AuthorizeCodePayload.class);
        if (authCodePayload == null) {
            // log.info(" token exchange {}" , "invalid cod")
            throw new ResourceException("Code is invalid or expired");
        }
        // if (!authCodePayload.getClient_Id().equals(client_id))
        //     throw new ResourceException("Client  Id did not matched");
        // Todo validation of app and client Secret
        // User user = cacheUtils.get("logedInUser", authCodePayload.getUserId(),
        // User.class);
        System.out.println("logedInUser::" + authCodePayload.getUserId());
        User user = redisService.getFromCache("logedInUser::" + authCodePayload.getUserId(), User.class);
        if (user == null)
            throw new ResourceException("User not find");
        // Todo : User - app link implmentation
        String accessToken = jwtService.createOauth2AccessToken(authCodePayload.getUserId(), client_id);
        String refreshToken = jwtService.createreOauth2refreshToken(authCodePayload.getUserId(), client_id);
        String idToken = jwtService.createIdToken(authCodePayload.getNonce(), user,
                authCodePayload.getClient_Id());
        return new String[] { accessToken, refreshToken, idToken };
        // return new String[] {};
    }
}
