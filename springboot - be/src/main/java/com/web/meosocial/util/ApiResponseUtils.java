package com.web.meosocial.util;

import com.web.meosocial.payload.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiResponseUtils {
    public <T> ApiResponseDto<T> success(T response, String message) {
        return ApiResponseDto.<T>builder()
                .status(String.valueOf(HttpStatus.OK))
                .message(List.of(message))
                .response(response)
                .build();
    }

    public <T> ApiResponseDto<T> error(HttpStatus status, String message) {
        return ApiResponseDto.<T>builder()
                .status(String.valueOf(status))
                .message(List.of(message))
                .response(null)
                .build();
    }
}
