package com.web.meosocial.auth.service;

import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import com.web.meosocial.payload.request.LoginRequest;
import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApiResponse<?>> register(RegisterRequest request) throws UserAlreadyExistsException;

    ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request);

    ResponseEntity<ApiResponse<?>> refreshAccessToken(String refreshToken);

    ResponseEntity<ApiResponse<?>> verifyEmail(String email, String code) throws RoleNotFoundException;

    ResponseEntity<ApiResponse<?>> resendEmailVerificationCode(String email);
}
