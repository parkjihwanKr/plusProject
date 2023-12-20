package com.pjh.plusproject.Global.Jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "Redis Provider")
@Component
public class RedisProvider {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisProvider(StringRedisTemplate redisTemplate, ObjectMapper objectMapper){
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void save(String key, Integer minutes, T value){
        String valueString = null;
        try{
            valueString = !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        }catch (JsonProcessingException e){
            throw new RuntimeException();
        }

        redisTemplate.opsForValue().set(key, valueString);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
    }

    public String getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getKey(String key, Class<T> valueType) {

        String value = getKey(key);
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteKey(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }
}
