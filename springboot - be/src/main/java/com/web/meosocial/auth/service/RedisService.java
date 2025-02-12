package com.web.meosocial.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    void saveToken(Long userId, String token, Long expiration);

    String getToken(Long userId);

    void deleteToken(Long userId);

    boolean checkToken(Long userId, String token);
}
