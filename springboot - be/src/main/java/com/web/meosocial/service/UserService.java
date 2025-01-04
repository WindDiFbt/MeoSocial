package com.web.meosocial.service;

import com.web.meosocial.dto.ChangePasswordDto;
import com.web.meosocial.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

    UserDto addUser(UserDto userDto);

    UserDto changePassword(ChangePasswordDto changePasswordDto);

    UserDto updateStatus(Long id, UserDto userDto);

//    UserDto loginUser(UserDto userDto);
}
