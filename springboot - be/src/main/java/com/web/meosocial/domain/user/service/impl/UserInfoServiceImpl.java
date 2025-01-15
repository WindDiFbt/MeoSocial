package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.model.UserInfo;
import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.domain.user.repository.UserInfoRepository;
import com.web.meosocial.cloudinary.CloudinaryService;
import com.web.meosocial.domain.user.service.UserInfoService;
import com.web.meosocial.domain.validation.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        return userInfoRepository.findById(userId).stream().map(UserInfoDto::new).findFirst().orElse(null);
    }

    /**
     * This method using to update information of user. Using Reflection to update field not null.
     *
     * @param userInfoDto input user information
     * @return UserInfoDto
     */
    @Override
    public UserInfoDto updateInformationUser(UserInfoDto userInfoDto) {
        UserInfo userInfo = userInfoRepository.findById(userInfoDto.getId()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        validationService.getUserInfoUpdateError(userInfoDto);
        Field[] fields = userInfoDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().equals("id")) {
                    continue;
                }
                Object value = field.get(userInfoDto);
                if (value != null) {
                    Field userField = UserInfo.class.getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(userInfo, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Error while updating user field: " + field.getName(), e);
            }
        }
        userInfoRepository.save(userInfo);
        return new UserInfoDto(userInfo);
    }

    @Override
    public void updateUserAvatar(Long userId, MultipartFile file) throws IOException {
        UserInfo userInfo = userInfoRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        String imageUrl = cloudinaryService.getImageUrlAfterUpload(file, Enums.FolderCloudinary.Avatar.toString());
        if (imageUrl == null) {
            throw new RuntimeException("Error while uploading image to Cloudinary: Wrong image format.");
        }
        userInfo.setAvatarUrl(imageUrl);
        userInfoRepository.save(userInfo);
    }
}
