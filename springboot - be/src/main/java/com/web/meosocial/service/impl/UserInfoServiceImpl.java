package com.web.meosocial.service.impl;

import com.web.meosocial.domain.UserInfo;
import com.web.meosocial.dto.UserInfoDto;
import com.web.meosocial.repository.UserInfoRepository;
import com.web.meosocial.service.UserInfoService;
import com.web.meosocial.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ValidationService validationService;
    private final String FILE_PATH = "upload/avatars/";

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        return userInfoRepository.findById(userId).stream().map(UserInfoDto::new).findFirst().orElse(null);
    }

    //This method using to update information of user. Using Reflection to update field not null
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
        String fileName = userId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(FILE_PATH, fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        userInfo.setAvatarUrl(filePath.toString());
        userInfoRepository.save(userInfo);
    }
}
