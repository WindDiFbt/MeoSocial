package com.web.meosocial.service;

import com.web.meosocial.dto.ChangePasswordDto;
import com.web.meosocial.dto.UserDto;
import com.web.meosocial.dto.UserInfoDto;
import org.springframework.stereotype.Service;

@Service
public interface ValidationService {
    void getUserRegisterError(UserDto userDto);

    void getUserInfoUpdateError(UserInfoDto userInfoDto);

    void getUserChangePasswordError(ChangePasswordDto changePasswordDto);
}
