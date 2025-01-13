package com.web.meosocial.service;

import com.web.meosocial.dto.ChangePasswordDto;
import com.web.meosocial.dto.UserDto;
import com.web.meosocial.dto.UserInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ValidationService {
    void getUserRegisterError(UserDto userDto);

    void getUserInfoUpdateError(UserInfoDto userInfoDto);

    void getUserChangePasswordError(ChangePasswordDto changePasswordDto);

    boolean isVideo(MultipartFile file) throws IOException;

    boolean isImage(MultipartFile file) throws IOException;
}
