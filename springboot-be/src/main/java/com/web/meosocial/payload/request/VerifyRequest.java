package com.web.meosocial.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {
    @NotBlank(message = "Email is required!")
    private String email;
    @NotBlank(message = "Code is required!")
    private String code;
}
