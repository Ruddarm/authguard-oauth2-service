package com.authguard.authguard_oauth2_service.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void saveToCache(String key, T Data, Duration ttl) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(Data);
        redisTemplate.opsForValue().set(key, json, ttl);
    }

    public <T> T getFromCache(String key, Class<T> targetClass) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null)
            return null;
        return objectMapper.readValue(json, targetClass);
    }

}
