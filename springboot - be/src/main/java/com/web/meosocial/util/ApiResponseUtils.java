package com.web.meosocial.util;

import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiResponseUtils {
    public <T> ApiResponse<T> success(T response, String message) {
        return ApiResponse.<T>builder()
                .status(String.valueOf(HttpStatus.OK))
                .message(List.of(message))
                .response(response)
                .build();
    }

    public <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(String.valueOf(status))
                .message(List.of(message))
                .response(null)
                .build();
    }
}
