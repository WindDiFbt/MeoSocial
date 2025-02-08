package com.web.meosocial.auth.service;

import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.payload.LoginRequestDto;
import com.web.meosocial.payload.RegisterRequestDto;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> login(LoginRequestDto request);

    ResponseEntity<ApiResponseDto<?>> register(RegisterRequestDto request) throws RoleNotFoundException, UserAlreadyExistsException;
}
