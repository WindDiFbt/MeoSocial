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
    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String VERIFY_EMAIL_PREFIX = "verify:email:";
    private static final String REGISTRATION_PREFIX = "register:";
    private static final String COOLDOWN_KEY_PREFIX = "resend:cooldown:";

    @Override
    public void blacklistToken(String accessToken, long expirationTimeInSeconds) {
        stringRedisTemplate.opsForValue().set(accessToken, BLACKLIST_PREFIX, expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        return stringRedisTemplate.hasKey(accessToken);
    }

    @Override
    public void cacheVerifyCode(String email, String code) {
        String key = VERIFY_EMAIL_PREFIX + email;
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    @Override
    public boolean isVerifyCodeCached(String email, String code) {
        String key = VERIFY_EMAIL_PREFIX + email;
        String cachedCode = stringRedisTemplate.opsForValue().get(key);
        return code.equals(cachedCode);
    }

    @Override
    public void removeCachedVerifyCode(String email) {
        String key = VERIFY_EMAIL_PREFIX + email;
        if (stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.delete(key);
        }
    }

    @Override
    public void cachePendingRegistration(RegisterRequest registerRequest) {
        String key = REGISTRATION_PREFIX + registerRequest.getEmail();
        redisTemplate.opsForValue().set(key, registerRequest, 15, TimeUnit.MINUTES);
    }

    @Override
    public RegisterRequest getAndRemovePendingRegistration(String email) {
        String key = REGISTRATION_PREFIX + email;
        RegisterRequest registerRequest = (RegisterRequest) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return registerRequest;
    }

    @Override
    public boolean isPendingRegistrationExist(String email) {
        String key = REGISTRATION_PREFIX + email;
        return redisTemplate.hasKey(key);
    }

    @Override
    public void setResendCooldown(String email, long cooldownTimeInSeconds) {
        stringRedisTemplate.opsForValue().set(COOLDOWN_KEY_PREFIX + email, "active", cooldownTimeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isResendCooldownActive(String email) {
        return stringRedisTemplate.hasKey(COOLDOWN_KEY_PREFIX + email);
    }
}
