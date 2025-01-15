package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.repository.UserRepository;
import com.web.meosocial.domain.user.service.UserRoleService;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validation.service.ValidationService;
import com.web.meosocial.util.UUID64Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ValidationService validationService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UUID64Generator uuid64Generator = new UUID64Generator();

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userDto.getUserName() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }
        Optional<User> list = userRepository.findByUserName(userDto.getUserName());
        if (list.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        validationService.getUserRegisterError(userDto);
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setId(uuid64Generator.generateUUID64());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserStatus(Enums.UserStatus.AVAILABLE.getValue());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        userRoleService.assignRole(user.getId(), Enums.RoleNames.ROLE_USER.toString());
        return new UserDto(user);
    }

    @Override
    public UserDto changePassword(ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(changePasswordDto.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            validationService.getUserChangePasswordError(changePasswordDto);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return new UserDto(user);
        }
        throw new IllegalArgumentException("Wrong old password");
    }

    @Override
    public UserDto updateStatus(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setUserStatus(userDto.getUserStatus());
        userRepository.save(user);
        return new UserDto(user);
    }
}
