package com.web.meosocial.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;
}
