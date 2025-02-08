package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.JwtUtils;
import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.payload.LoginRequestDto;
import com.web.meosocial.payload.LoginResponseDto;
import com.web.meosocial.payload.RegisterRequestDto;
import com.web.meosocial.auth.service.AuthService;
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
import com.web.meosocial.util.UUID64Generator;
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
    private final UUID64Generator uuid64Generator = new UUID64Generator();

    @Override
    public ResponseEntity<ApiResponseDto<?>> login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUserName(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .username(userDetails.getUsername())
                .id(userDetails.getId())
                .token(token)
                .type("Bearer")
                .roles(roles)
                .build();
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(String.valueOf(HttpStatus.OK))
                        .message(List.of("Successfully logged in!"))
                        .response(loginResponseDto)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> register(RegisterRequestDto registerRequestDto) throws RoleNotFoundException, UserAlreadyExistsException {
        if (userService.existsByUserName(registerRequestDto.getUserName())) {
            throw new UserAlreadyExistsException("User name already exists!");
        }

        User user = createUser(registerRequestDto);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.builder()
                        .status(String.valueOf(HttpStatus.CREATED))
                        .message(List.of("User account has been created successfully!"))
                        .build()
        );
    }

    private User createUser(RegisterRequestDto registerRequestDto) throws RoleNotFoundException {
        validationService.getUserRegisterError(registerRequestDto);
        return User.builder()
                .id(uuid64Generator.generateUUID64())
                .userName(registerRequestDto.getUserName())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .userStatus(Enums.UserStatus.AVAILABLE.getValue())
                .createdAt(LocalDateTime.now())
                .roles(mapRoles(determineRoles(registerRequestDto.getRoles())))
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
}
