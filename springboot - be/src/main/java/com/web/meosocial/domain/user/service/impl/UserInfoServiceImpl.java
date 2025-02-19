package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.cloudinary.CloudinaryService;
import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.model.UserInfo;
import com.web.meosocial.domain.user.repository.UserInfoRepository;
import com.web.meosocial.domain.user.service.UserInfoService;
import com.web.meosocial.domain.user.service.UserService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.ApiResponseDto;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private UserService userService;
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    @Override
    public ApiResponseDto<UserInfoDto> getUserInfo(Long userId) {
        User user = userService.getUserById(userId);
        return apiResponseUtils.success(userInfoRepository.findById(user.getId()).stream().map(UserInfoDto::new)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("User not found!")), "Get user info success");
    }

    /**
     * This method using to update information of user. Using Reflection to update field not null.
     *
     * @param userInfoDto input user information
     * @return UserInfoDto
     */
    @Transactional
    @Override
    public ApiResponseDto<UserInfoDto> updateInformationUser(Long userId, UserInfoDto userInfoDto) {
        if (!userInfoDto.getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to update user info");
        }
        User user = userService.getUserById(userId);
        UserInfo userInfo = userInfoRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
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
        return apiResponseUtils.success(new UserInfoDto(userInfo), "Update user info success");
    }

    @Transactional
    @Override
    public ApiResponseDto<Void> updateUserAvatar(Long userId, MultipartFile file) throws IOException {
        User user = userService.getUserById(userId);
        UserInfo userInfo = userInfoRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        String imageUrl = cloudinaryService.getImageUrlAfterUpload(file, Enums.FolderCloudinary.Avatar.toString());
        if (imageUrl == null) {
            throw new RuntimeException("Error while uploading image to Cloudinary: Wrong image format.");
        }
        userInfo.setAvatarUrl(imageUrl);
        userInfoRepository.save(userInfo);
        return apiResponseUtils.success(null, "Update user's avatar success");
    }
}
