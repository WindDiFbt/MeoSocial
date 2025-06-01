package com.web.meosocial.auth.service;

public interface VerificationService {
    void sendVerificationCode(String email);

    boolean verifyCode(String email, String code);
}
