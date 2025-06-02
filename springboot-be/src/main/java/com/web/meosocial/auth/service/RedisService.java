package com.web.meosocial.auth.service;

import com.web.meosocial.payload.request.RegisterRequest;

public interface RedisService {
    void blacklistToken(String accessToken, long expirationTimeInSeconds);

    boolean isTokenBlacklisted(String accessToken);

    void cacheVerifyCode(String email, String code);

    boolean isVerifyCodeCached(String email, String code);

    void removeCachedVerifyCode(String email);

    void cachePendingRegistration(RegisterRequest registerRequest);

    RegisterRequest getAndRemovePendingRegistration(String email);

    boolean isPendingRegistrationExist(String email);

    void setResendCooldown(String email, long cooldownTimeInSeconds);

    boolean isResendCooldownActive(String email);
}
