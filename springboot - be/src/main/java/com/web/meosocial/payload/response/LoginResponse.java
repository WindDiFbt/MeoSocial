package com.web.meosocial.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private String id;
    private String username;
    private List<String> roles;
}
