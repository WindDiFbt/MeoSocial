package com.web.meosocial.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiResponseDto<T> {
    private String status;
    private List<String> message;
    private T response;
}
