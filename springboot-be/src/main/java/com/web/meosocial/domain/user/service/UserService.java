package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ApiResponse<List<UserDto>> findAll();

    ApiResponse<UserDto> changePassword(Long userId, ChangePasswordDto changePasswordDto);

    ApiResponse<UserDto> updateStatus(Long userId, Integer status);

    User getUserById(Long userId);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    void saveUser(User user);
}
