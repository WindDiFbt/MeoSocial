package com.web.meosocial.domain.validator.service;

import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ValidationService {
    void getUserRegisterError(RegisterRequest registerRequest);

    void getUserInfoUpdateError(UserInfoDto userInfoDto);

    void getUserChangePasswordError(ChangePasswordDto changePasswordDto);

    boolean isVideo(MultipartFile file) throws IOException;

    boolean isImage(MultipartFile file) throws IOException;
}
