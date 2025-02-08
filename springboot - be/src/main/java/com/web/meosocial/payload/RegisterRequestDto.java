package com.web.meosocial.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String userName;
    private String password;
    private List<String> roles;
}
