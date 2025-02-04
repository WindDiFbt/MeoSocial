package com.web.meosocial.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "User Name is required!")
    private String userName;

    @NotBlank(message = "Password is required!")
    private String password;
}
