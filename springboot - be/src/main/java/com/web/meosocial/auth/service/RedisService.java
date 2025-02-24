package com.web.meosocial.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    void blacklistToken(String accessToken, long expirationTimeInSeconds);

    boolean isTokenBlacklisted(String accessToken);
}
