package com.web.meosocial.auth.service;

import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.payload.request.LoginRequest;
import com.web.meosocial.payload.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<ApiResponse<?>> login(LoginRequest loginRequest, HttpServletRequest request);

    ResponseEntity<ApiResponse<?>> register(RegisterRequest request) throws RoleNotFoundException, UserAlreadyExistsException;

    ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request);

    ResponseEntity<ApiResponse<?>> refreshToken(String refreshToken);
}
