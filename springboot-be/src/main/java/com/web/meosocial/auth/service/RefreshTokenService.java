package com.web.meosocial.auth.service;

import com.web.meosocial.auth.models.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId, String ipDevice);

    boolean validateRefreshToken(String refreshToken);

    void deleteRefreshToken(Long userId, String ipDevice);

    Long getUserIdByRefreshToken(String refreshToken);

    void setRefreshTokenCookie(HttpServletResponse response, String refreshToken);
}
