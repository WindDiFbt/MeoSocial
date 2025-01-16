package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

    UserDto addUser(UserDto userDto);

    UserDto changePassword(ChangePasswordDto changePasswordDto);

    UserDto updateStatus(Long id, UserDto userDto);

    User getUserById(Long id);
}
