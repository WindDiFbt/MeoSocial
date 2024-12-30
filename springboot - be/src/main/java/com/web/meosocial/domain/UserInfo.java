package com.web.meosocial.domain;

import com.web.meosocial.dto.UserInfoDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "userinfo")
public class UserInfo {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "study_at", length = 127)
    private String studyAt;

    @Column(name = "working_at", length = 127)
    private String workingAt;

    @Column(name = "favorites", length = 1023)
    private String favorites;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor to convert UserInfoDto to UserInfo entity
    public UserInfo(UserInfoDto userInfoDto) {
        if (userInfoDto != null) {
            this.id = userInfoDto.getId();
            this.gender = userInfoDto.getGender();
            this.isActive = userInfoDto.getIsActive();
            this.studyAt = userInfoDto.getStudyAt();
            this.workingAt = userInfoDto.getWorkingAt();
            this.favorites = userInfoDto.getFavorites();
            this.dateOfBirth = userInfoDto.getDateOfBirth();
            this.createdAt = userInfoDto.getCreatedAt();
            this.updatedAt = userInfoDto.getUpdatedAt();
            if (userInfoDto.getUserId() != null) {
                User user = new User();
                user.setId(userInfoDto.getUserId());
                this.user = user;
            }
        }
    }
}