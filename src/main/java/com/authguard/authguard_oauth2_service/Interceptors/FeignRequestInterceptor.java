package com.authguard.authguard_oauth2_service.Interceptors;

import org.springframework.stereotype.Component;

import com.authguard.authguard_oauth2_service.Context.AccessToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", AccessToken.get());
    }

}
