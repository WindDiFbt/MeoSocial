package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.models.RefreshToken;
import com.web.meosocial.auth.repository.RefreshTokenRepository;
import com.web.meosocial.auth.service.RefreshTokenService;
import com.web.meosocial.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenExpirationTime;

    @Transactional
    @Override
    public RefreshToken createRefreshToken(Long userId, String ipDevice) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationTime));
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setIpDevice(ipDevice);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenExisted = refreshTokenRepository.findByToken(refreshToken);
        return refreshTokenExisted.isPresent() && refreshTokenExisted.get().getExpiryDate().isAfter(Instant.now());
    }

    @Transactional
    @Override
    public void deleteRefreshToken(Long userId, String ipDevice) {
        refreshTokenRepository.deleteByUserIdAndIpDevice(userId, ipDevice);
    }

    @Override
    public Long getUserIdByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken).get().getUser().getId();
    }

    @Override
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setMaxAge(refreshTokenExpirationTime.intValue());
        response.addCookie(cookie);
    }
}
