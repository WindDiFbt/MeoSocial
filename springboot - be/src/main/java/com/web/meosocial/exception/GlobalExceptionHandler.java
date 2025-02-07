package com.web.meosocial.exception;

import com.web.meosocial.auth.dto.ApiResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> handleException(IllegalArgumentException e) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("Messages", e.getMessage().trim().split("\n"));
//        errorResponse.put("Error Code", HttpStatus.BAD_REQUEST.value());
//        return ResponseEntity.badRequest().body(errorResponse);
//    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<?>> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponseDto.builder()
                        .status(String.valueOf(HttpStatus.BAD_REQUEST))
                        .message(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiResponseDto<?>> validationExceptionHandler(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponseDto.builder()
                                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                                .message(ex.getErrors())
                                .build());
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponseDto.builder()
                                .status(String.valueOf(HttpStatus.CONFLICT))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> RoleNotFoundExceptionHandler(RoleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .status(String.valueOf(HttpStatus.NOT_FOUND))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto<?>> UnauthorizedExceptionHandler(UnauthorizedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponseDto.builder()
                                .status(String.valueOf((HttpStatus.UNAUTHORIZED)))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> handleException(RuntimeException e) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("Messages", e.getMessage().trim().split("\n"));
//        errorResponse.put("Error Code", HttpStatus.BAD_REQUEST.value());
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
}
