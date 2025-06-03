package com.web.meosocial.auth.service;

public interface VerificationService {
    void sendVerificationEmailCode(String email);

    boolean verifyCode(String email, String code);

    void resendVerificationEmailCode(String email);
}
