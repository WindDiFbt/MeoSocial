package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.repository.UserRepository;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ApiResponseDto<List<UserDto>> findAll() {
        return apiResponseUtils.success(userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList()), "Get all users success");
    }

    @Transactional
    @Override
    public ApiResponseDto<UserDto> changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = getUserById(userId);
        if (passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            validationService.getUserChangePasswordError(changePasswordDto);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return apiResponseUtils.success(new UserDto(user), "Change password success");
        }
        throw new IllegalArgumentException("Wrong old password");
    }

    @Transactional
    @Override
    public ApiResponseDto<UserDto> updateStatus(Long userId, Integer status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setUserStatus(status);
        userRepository.save(user);
        return apiResponseUtils.success(new UserDto(user), "Update status success");
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserStatus().equals(Enums.UserStatus.NOT_AVAILABLE.getValue())) {
            throw new IllegalArgumentException("User not found or not available.");
        }
        return user;
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
