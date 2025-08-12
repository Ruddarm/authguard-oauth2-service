package com.authguard.authguard_oauth2_service.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class Config {

        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFatory) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                // objectMapper.activateDefaultTyping(
                // LaissezFaireSubTypeValidator.instance,
                // ObjectMapper.DefaultTyping.NON_FINAL);

                // ✅ Specify generic type explicitly (e.g., Object)
                Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                                Object.class);
                Map<String, RedisCacheConfiguration> cacheMangaer = new HashMap<>();
                cacheMangaer.put("logedInUser",
                                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
                                                .enableTimeToIdle()
                                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                                .fromSerializer(serializer)));
                // cacheMangaer.put("AuthorizeCode",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)).enableTimeToIdle());
                cacheMangaer.put("AuthorizeCode",
                                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3))
                                                .enableTimeToIdle()
                                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                                .fromSerializer(serializer)));
                return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFatory))
                                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                                .fromSerializer(serializer)))
                                .withInitialCacheConfigurations(cacheMangaer).build();
        }

        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
                RedisTemplate<String, String> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory);
                // Key Serializer
                template.setKeySerializer(new StringRedisSerializer());
                template.setHashKeySerializer(new StringRedisSerializer());
                // Value Serializer as plain String (JSON string)
                template.setValueSerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new StringRedisSerializer());
                template.afterPropertiesSet();
                return template;
        }

}
