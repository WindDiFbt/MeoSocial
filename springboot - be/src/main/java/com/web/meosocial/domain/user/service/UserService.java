package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.exception.RoleNotFoundException;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ApiResponseDto<List<UserDto>> findAll();

    ApiResponseDto<UserDto> changePassword(Long userId, ChangePasswordDto changePasswordDto);

    ApiResponseDto<UserDto> updateStatus(Long userId, Integer status);

    User getUserById(Long userId);

    boolean existsByUserName(String username);

    void saveUser(User user);
}
