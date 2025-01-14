package com.web.meosocial.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordDto {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
