package com.web.meosocial.dto;

import com.web.meosocial.domain.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private Long userId;
    private String gender;
    private Boolean isActive;
    private String studyAt;
    private String workingAt;
    private String favorites;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor to map from UserInfo entity to UserInfoDto
    public UserInfoDto(UserInfo userInfo) {
        if (userInfo != null) {
            this.id = userInfo.getId();
            this.userId = userInfo.getUser() != null ? userInfo.getUser().getId() : null;
            this.gender = userInfo.getGender();
            this.isActive = userInfo.getIsActive();
            this.studyAt = userInfo.getStudyAt();
            this.workingAt = userInfo.getWorkingAt();
            this.favorites = userInfo.getFavorites();
            this.dateOfBirth = userInfo.getDateOfBirth();
            this.createdAt = userInfo.getCreatedAt();
            this.updatedAt = userInfo.getUpdatedAt();
        }
    }
}
