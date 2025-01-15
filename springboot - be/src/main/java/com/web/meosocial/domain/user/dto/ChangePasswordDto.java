package com.web.meosocial.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordDto {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
