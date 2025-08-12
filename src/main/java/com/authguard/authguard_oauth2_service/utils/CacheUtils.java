package com.authguard.authguard_oauth2_service.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.authguard.authguard_oauth2_service.Exception.ResourceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheUtils {
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    public <T> T get(String cacheNames, String key, Class<T> clazz) throws ResourceException {
        Cache cache = cacheManager.getCache(cacheNames);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null && wrapper.get() instanceof String json) {
                try {
                    return objectMapper.readValue(json, clazz);
                } catch (JsonProcessingException e) {
                    throw new ResourceException("Invalid Authoriation code payload");
                }
            }
        }
        return null;
    }
      public <T> T getPayload(String cacheNames, String key, Class<T> clazz) throws ResourceException {
        Cache cache = cacheManager.getCache(cacheNames);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(key);
            if (wrapper != null && wrapper.get() instanceof String json) {
                try {
                    return objectMapper.readValue(json, clazz);
                } catch (JsonProcessingException e) {
                    throw new ResourceException("Invalid Authoriation code payload");
                }
            }
        }
        return null;
    }

    public void put(String cacheNames, String key, Object value) {
        Cache cache = cacheManager.getCache(cacheNames);
        if (cache != null) {
            try {
                String json = objectMapper.writeValueAsString(value);
                cache.put(key, json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
