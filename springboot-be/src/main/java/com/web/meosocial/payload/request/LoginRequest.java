package com.web.meosocial.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "User Name, password or phone number!")
    private String identifier;

    @NotBlank(message = "Password is required!")
    private String password;
}
