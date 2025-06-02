package com.web.meosocial.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {
    @NotBlank(message = "Email is required!")
    private String email;
    private String code;
}
