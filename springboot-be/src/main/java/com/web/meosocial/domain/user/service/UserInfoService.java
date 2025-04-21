package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserInfoService {
    ApiResponse<UserInfoDto> getUserInfo(Long userId);

    ApiResponse<UserInfoDto> updateInformationUser(Long userId, UserInfoDto userInfoDto);

    ApiResponse<Void> updateUserAvatar(Long userId, MultipartFile file) throws IOException;
}
