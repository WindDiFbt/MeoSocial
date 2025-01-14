package com.web.meosocial.service.user;

import com.web.meosocial.dto.user.ChangePasswordDto;
import com.web.meosocial.dto.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

    UserDto addUser(UserDto userDto);

    UserDto changePassword(ChangePasswordDto changePasswordDto);

    UserDto updateStatus(Long id, UserDto userDto);
}
