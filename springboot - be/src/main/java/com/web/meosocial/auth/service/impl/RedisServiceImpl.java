package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_PREFIX = "auth:token:";

    @Override
    public void saveToken(Long userId, String token, Long expiration) {
        String key = TOKEN_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, token, Duration.ofSeconds(expiration));
    }

    @Override
    public String getToken(Long userId) {
        return stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
    }

    @Override
    public void deleteToken(Long userId) {
        stringRedisTemplate.delete(TOKEN_PREFIX + userId);
    }

    @Override
    public boolean checkToken(Long userId, String token) {
        String storedToken = getToken(userId);
        return storedToken != null && storedToken.equals(token);
    }
}
