package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.UserInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserInfoService {
    UserInfoDto getUserInfo(Long userId);

    UserInfoDto updateInformationUser(UserInfoDto userInfoDto);

    void updateUserAvatar(Long userId, MultipartFile file) throws IOException;
}
