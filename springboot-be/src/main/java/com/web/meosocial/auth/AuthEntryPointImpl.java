package com.web.meosocial.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.meosocial.exception.GlobalExceptionHandler;
import com.web.meosocial.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AuthEntryPointImpl implements AuthenticationEntryPoint {
    @Autowired
    @Qualifier("globalExceptionHandler")
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        globalExceptionHandler.AuthenticationExceptionHandler(authException);
        if (authException instanceof InsufficientAuthenticationException) {
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(
                    ApiResponse.builder().
                            status(String.valueOf(HttpStatus.UNAUTHORIZED))
                            .message(List.of("Full authentication is required to access this resource"))
                            .build()));
        }
    }
}