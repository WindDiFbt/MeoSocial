package com.web.meosocial.auth;

import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.payload.LoginRequestDto;
import com.web.meosocial.payload.RegisterRequestDto;
import com.web.meosocial.auth.service.AuthService;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<?>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<?>> register(@RequestBody @Valid RegisterRequestDto registerRequestDto)
            throws RoleNotFoundException, UserAlreadyExistsException {
        return authService.register(registerRequestDto);
    }
}
