package com.web.meosocial.auth.service;

import com.web.meosocial.auth.models.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId, String ipDevice);

    boolean validateRefreshToken(String refreshToken);

    void deleteRefreshToken(Long userId, String ipDevice);

    Long getUserIdByRefreshToken(String refreshToken);
}
