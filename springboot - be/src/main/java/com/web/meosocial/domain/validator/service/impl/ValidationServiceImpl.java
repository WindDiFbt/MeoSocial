package com.web.meosocial.domain.validator.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.post.model.Post;
import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserInfoDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.service.UserRelationshipService;
import com.web.meosocial.domain.validator.service.ValidationService;
import com.web.meosocial.exception.ValidationException;
import com.web.meosocial.payload.request.RegisterRequest;
import com.web.meosocial.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private UserRelationshipService userRelationshipService;

    @Override
    public void getUserRegisterError(RegisterRequest registerRequest) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtils.validateUsername(registerRequest.getUserName()));
        errorMessages.add(ValidationUtils.validatePassword(registerRequest.getPassword()));
        throwError(errorMessages);
    }

    @Override
    public void getUserInfoUpdateError(UserInfoDto userInfoDto) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtils.validateFullName(userInfoDto.getFullName()));
        errorMessages.add(ValidationUtils.validateEmail(userInfoDto.getEmail()));
        errorMessages.add(ValidationUtils.validatePhone(userInfoDto.getPhoneNumber()));
        errorMessages.add(ValidationUtils.validateDOB(userInfoDto.getDateOfBirth()));
        throwError(errorMessages);
    }

    @Override
    public void getUserChangePasswordError(ChangePasswordDto changePasswordDto) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(ValidationUtils.validatePassword(changePasswordDto.getNewPassword()));
        throwError(errorMessages);
    }

    private void throwError(List<String> errorMessages) {
        errorMessages.removeIf(e -> e == null || e.trim().isEmpty());
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
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
    /**
     * Checks if a user has permission to interact with a post based on its visibility level.
     * <p>
     * This method determines whether the user can perform actions (such as commenting) on a given post.
     * It considers factors like whether the user is blocked, the post's visibility level, and the user's
     * relationship with the post's owner.
     *
     * @param user The user attempting to interact with the post.
     * @param post The post being accessed.
     * @param visibilityLevel The visibility setting of the post.
     * @return true if the user does NOT have permission to interact with the post, false otherwise.
     */
    @Override
    public boolean hasNotPermissionToAction(User user, Post post, Enums.VisibilityLevel visibilityLevel) {
        if (userRelationshipService.IsUserBlocked(user.getId(), post.getUser().getId()) || userRelationshipService.IsUserBlocked(post.getUser().getId(), user.getId())) {
            return true;
        }
        boolean hasAccess = switch (visibilityLevel) {
            case PUBLIC -> true;
            case FRIENDS ->
                    userRelationshipService.IsUserRelaMutualFollow(user.getId(), post.getUser().getId()) || user.getId().equals(post.getUser().getId());
            case PRIVATE -> user.getId().equals(post.getUser().getId());
            case FOLLOWER ->
                    userRelationshipService.IsUserFollow(user.getId(), post.getUser().getId()) || user.getId().equals(post.getUser().getId());
            default -> false;
        };
        return !hasAccess;
    }
}
