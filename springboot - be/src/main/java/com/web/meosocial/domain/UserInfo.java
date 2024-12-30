package com.web.meosocial.domain;

import com.web.meosocial.dto.UserInfoDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(name = "full_name", length = 127)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number", length = 25)
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "interested_user", length = 258)
    private String interestedUser;

    @Lob
    @Column(name = "gender")
    private Integer gender;

    @Column(name = "study_at", length = 127)
    private String studyAt;

    @Column(name = "working_at", length = 127)
    private String workingAt;

    @Column(name = "favorites", length = 1023)
    private String favorites;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Constructor to convert UserInfoDto to UserInfo entity
    public UserInfo(UserInfoDto userInfoDto) {
        if (userInfoDto != null) {
            this.id = userInfoDto.getId();
            this.fullName = userInfoDto.getFullName();
            this.email = userInfoDto.getEmail();
            this.phoneNumber = userInfoDto.getPhoneNumber();
            this.avatarUrl = userInfoDto.getAvatarUrl();
            this.interestedUser = userInfoDto.getInterestedUser();
            this.gender = userInfoDto.getGender();
            this.studyAt = userInfoDto.getStudyAt();
            this.workingAt = userInfoDto.getWorkingAt();
            this.favorites = userInfoDto.getFavorites();
            this.dateOfBirth = userInfoDto.getDateOfBirth();
        }
    }

}