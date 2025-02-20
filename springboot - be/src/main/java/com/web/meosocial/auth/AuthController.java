package com.web.meosocial.auth;

import com.web.meosocial.auth.service.AuthService;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.payload.request.LoginRequest;
import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.payload.request.RefreshTokenRequest;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        return authService.login(loginRequest, request);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegisterRequest registerRequest)
            throws RoleNotFoundException, UserAlreadyExistsException {
        return authService.register(registerRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }
}
