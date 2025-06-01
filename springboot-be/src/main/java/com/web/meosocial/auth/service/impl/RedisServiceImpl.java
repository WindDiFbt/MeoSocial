package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.service.RedisService;
import com.web.meosocial.payload.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void blacklistToken(String accessToken, long expirationTimeInSeconds) {
        stringRedisTemplate.opsForValue().set(accessToken, "blacklisted", expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        return stringRedisTemplate.hasKey(accessToken);
    }

    @Override
    public void cacheVerifyCode(String email, String code) {
        String key = "verify:email:" + email;
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    @Override
    public boolean isVerifyCodeCached(String email, String code) {
        String key = "verify:email:" + email;
        String cachedCode = stringRedisTemplate.opsForValue().get(key);
        return code.equals(cachedCode);
    }

    @Override
    public void removeCachedVerifyCode(String email) {
        String key = "verify:email:" + email;
        if (stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.delete(key);
        }
    }

    @Override
    public void cachePendingRegister(RegisterRequest registerRequest) {
        redisTemplate.opsForValue().set("register:" + registerRequest.getEmail(), registerRequest, 5, TimeUnit.MINUTES);
    }

    @Override
    public RegisterRequest getPendingRegister(String email) {
        RegisterRequest registerRequest = (RegisterRequest) redisTemplate.opsForValue().get("register:" + email);
        redisTemplate.delete("register:" + email);
        return registerRequest;
    }
}
