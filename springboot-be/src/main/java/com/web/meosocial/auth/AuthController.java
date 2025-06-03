package com.web.meosocial.auth;

import com.web.meosocial.auth.service.AuthService;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import com.web.meosocial.payload.request.LoginRequest;
import com.web.meosocial.payload.request.RefreshTokenRequest;
import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.payload.request.VerifyRequest;
import com.web.meosocial.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        return authService.login(loginRequest, request, response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegisterRequest registerRequest)
            throws UserAlreadyExistsException {
        return authService.register(registerRequest);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verify(@RequestBody @Valid VerifyRequest verifyRequest)
            throws RoleNotFoundException {
        return authService.verifyEmail(verifyRequest.getEmail(), verifyRequest.getCode());
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<?>> resendVerificationCode(@RequestBody @Valid VerifyRequest verifyRequest) {
        return authService.resendEmailVerificationCode(verifyRequest.getEmail());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
    }
}
