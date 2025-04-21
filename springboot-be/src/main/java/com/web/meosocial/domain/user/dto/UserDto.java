package com.web.meosocial.domain.user.dto;

import com.web.meosocial.domain.user.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private Integer userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.userName = user.getUserName();
            this.password = user.getPassword();
            this.userStatus = user.getUserStatus();
            this.createdAt = user.getCreatedAt();
            this.updatedAt = user.getUpdatedAt();
        }
    }
}
