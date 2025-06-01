package com.web.meosocial.exception;

import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .status(String.valueOf(HttpStatus.BAD_REQUEST))
                        .message(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiResponse<?>> validationExceptionHandler(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                                .message(ex.getErrors())
                                .build());
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.CONFLICT))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> RoleNotFoundExceptionHandler(RoleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.NOT_FOUND))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> UnauthorizedExceptionHandler(UnauthorizedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.UNAUTHORIZED))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> HttpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ApiResponse<?>> NullPointerExceptionHandler(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> AuthenticationExceptionHandler(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.UNAUTHORIZED))
                                .message(List.of(exception.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ApiResponse<?>> BindExceptionHandler(BindException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                                .message(exception.getAllErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toList())
                                .build()
                );
    }
}
