package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.auth.JwtUtils;
import com.web.meosocial.auth.models.RefreshToken;
import com.web.meosocial.auth.service.AuthService;
import com.web.meosocial.auth.service.RedisService;
import com.web.meosocial.auth.service.RefreshTokenService;
import com.web.meosocial.auth.service.VerificationService;
import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.dto.RoleDto;
import com.web.meosocial.domain.user.model.Role;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.model.UserDetailsImpl;
import com.web.meosocial.domain.user.service.RoleService;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.exception.UserAlreadyExistsException;
import com.web.meosocial.payload.request.LoginRequest;
import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.payload.response.LoginResponse;
import com.web.meosocial.payload.response.RefreshTokenResponse;
import com.web.meosocial.util.ApiResponseUtils;
import com.web.meosocial.util.UUID64Generator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private VerificationService verificationService;
    private final UUID64Generator uuid64Generator = new UUID64Generator();

    @Override
    public ResponseEntity<ApiResponse<?>> login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        String ipDevice = authUtils.getDeviceId(request);
        refreshTokenService.deleteRefreshToken(userDetails.getId(), ipDevice);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), ipDevice);
        refreshTokenService.setRefreshTokenCookie(response, refreshToken.getToken());
        String accessToken = jwtUtils.generateAccessToken(authentication);
        LoginResponse loginResponse = LoginResponse.builder()
                .username(userDetails.getUsername())
                .id(String.valueOf(userDetails.getId()))
                .accessToken(accessToken)
                .type("Bearer")
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .build();
        return ResponseEntity.ok().body(
                apiResponseUtils.success(loginResponse, "Successfully logged in!")
        );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> register(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        if (registerRequest.getUsername() == null || userService.existsByUserName(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User name already exists!");
        }
        if (registerRequest.getEmail() == null || userService.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists!");
        }

        redisService.cachePendingRegister(registerRequest);
        verificationService.sendVerificationCode(registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(
                apiResponseUtils.success(null, "Please check your email for verification.")
        );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> verify(String email, String code) throws RoleNotFoundException {
        if (email == null || code == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    apiResponseUtils.error(HttpStatus.BAD_REQUEST, "Email and verification code must not be null!")
            );
        }
        if (!verificationService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    apiResponseUtils.error(HttpStatus.BAD_REQUEST, "Invalid verification code!")
            );
        }
        RegisterRequest registerRequest = redisService.getPendingRegister(email);
        if (registerRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    apiResponseUtils.error(HttpStatus.BAD_REQUEST, "No pending registration found for this email!")
            );
        }
        User user = createUser(registerRequest);
        userService.saveUser(user);
        redisService.removeCachedVerifyCode(email);
        return ResponseEntity.ok().body(
                apiResponseUtils.success(null, "Registration successful!"));
    }

    private User createUser(RegisterRequest registerRequest) throws RoleNotFoundException {
        validationService.getUserRegisterError(registerRequest);
        return User.builder()
                .id(uuid64Generator.generateUUID64())
                .userName(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .userStatus(Enums.UserStatus.AVAILABLE.getValue())
                .createdAt(LocalDateTime.now())
                .roles(mapRoles(List.of(roleService.getInstance("user"))))
                .build();
    }

    private List<Role> mapRoles(List<RoleDto> roleDtos) {
        List<Role> roles = new ArrayList<>();
        for (RoleDto roleDto : roleDtos) {
            roles.add(new Role(roleDto));
        }
        return roles;
    }

    private List<RoleDto> determineRoles(List<String> roleList) throws RoleNotFoundException {
        List<RoleDto> roles = new ArrayList<>();
        if (roleList == null) {
            roles.add(roleService.getInstance("user"));
        } else {
            for (String role : roleList) {
                roles.add(roleService.getInstance(role));
            }
        }
        return roles;
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        String accessToken = getToken(request);
        if (accessToken != null && jwtUtils.validateAccessToken(accessToken)) {
            String ipDevice = authUtils.getDeviceId(request);
            refreshTokenService.deleteRefreshToken(jwtUtils.claimUserId(accessToken), ipDevice);
            long expiration = jwtUtils.getExpiration(accessToken);
            redisService.blacklistToken(accessToken, expiration);
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().body(
                    apiResponseUtils.success(null, "Successfully logged out!")
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                apiResponseUtils.success(null, "Invalid token!")
        );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> refreshAccessToken(String refreshToken) {
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    apiResponseUtils.success(null, "Invalid refresh token!")
            );
        }
        Long userId = refreshTokenService.getUserIdByRefreshToken(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(userId);
        return ResponseEntity.ok().body(
                apiResponseUtils.success(
                        RefreshTokenResponse.builder()
                                .refreshToken(refreshToken)
                                .accessToken(newAccessToken).build(), "Successfully refreshed token!")
        );
    }
}
