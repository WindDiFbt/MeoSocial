package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void blacklistToken(String accessToken, long expirationTimeInSeconds) {
        stringRedisTemplate.opsForValue().set(accessToken, "blacklisted", expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(accessToken));
    }
}
