package com.web.meosocial.dto.user;

import com.web.meosocial.domain.user.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String interestedUser;
    private Integer gender;
    private String studyAt;
    private String workingAt;
    private String favorites;
    private LocalDate dateOfBirth;

    // Constructor to map from UserInfo entity to UserInfoDto
    public UserInfoDto(UserInfo userInfo) {
        if (userInfo != null) {
            this.id = userInfo.getId();
            this.fullName = userInfo.getFullName();
            this.email = userInfo.getEmail();
            this.phoneNumber = userInfo.getPhoneNumber();
            this.avatarUrl = userInfo.getAvatarUrl();
            this.interestedUser = userInfo.getInterestedUser();
            this.gender = userInfo.getGender();
            this.studyAt = userInfo.getStudyAt();
            this.workingAt = userInfo.getWorkingAt();
            this.favorites = userInfo.getFavorites();
            this.dateOfBirth = userInfo.getDateOfBirth();
        }
    }
}
