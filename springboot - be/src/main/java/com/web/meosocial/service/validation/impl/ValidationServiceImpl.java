package com.web.meosocial.service.validation.impl;

import com.web.meosocial.dto.user.ChangePasswordDto;
import com.web.meosocial.dto.user.UserDto;
import com.web.meosocial.dto.user.UserInfoDto;
import com.web.meosocial.service.validation.ValidationService;
import com.web.meosocial.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public void getUserRegisterError(UserDto userDto) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtil.validateUsername(userDto.getUserName()));
        errorMessages.add(ValidationUtil.validatePassword(userDto.getPassword()));
        throwError(errorMessages);
    }

    @Override
    public void getUserInfoUpdateError(UserInfoDto userInfoDto) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtil.validateFullName(userInfoDto.getFullName()));
        errorMessages.add(ValidationUtil.validateEmail(userInfoDto.getEmail()));
        errorMessages.add(ValidationUtil.validatePhone(userInfoDto.getPhoneNumber()));
        errorMessages.add(ValidationUtil.validateDOB(userInfoDto.getDateOfBirth()));
        throwError(errorMessages);
    }

    @Override
    public void getUserChangePasswordError(ChangePasswordDto changePasswordDto) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtil.validatePassword(changePasswordDto.getNewPassword()));
        throwError(errorMessages);
    }

    private void throwError(List<String> errorMessages) {
        errorMessages.removeIf(e -> e == null || e.trim().isEmpty());
        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errorMessages));
        }
    }

    @Override
    public boolean isVideo(MultipartFile file) {
        String mimeType = file.getContentType();
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("video/");
    }

    @Override
    public boolean isImage(MultipartFile file) {
        String mimeType = file.getContentType();
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("image/");
    }
}
