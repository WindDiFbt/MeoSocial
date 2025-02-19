package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserInfoService {
    ApiResponseDto<UserInfoDto> getUserInfo(Long userId);

    ApiResponseDto<UserInfoDto> updateInformationUser(Long userId, UserInfoDto userInfoDto);

    ApiResponseDto<Void> updateUserAvatar(Long userId, MultipartFile file) throws IOException;
}
