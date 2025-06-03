package com.web.meosocial.auth.service.impl;

import com.web.meosocial.auth.service.RedisService;
import com.web.meosocial.auth.service.VerificationService;
import com.web.meosocial.domain.mail.MailRequest;
import com.web.meosocial.domain.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class VerificationServiceImpl implements VerificationService {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @Override
    public void sendVerificationEmailCode(String email) {
        String code = generateVerificationCode();
        redisService.cacheVerifyCode(email, code);
        mailService.sendMailAsync(new MailRequest(email, "Registration verification code", "Your verification code is: " + code));
    }

    @Override
    public boolean verifyCode(String email, String code) {
        return redisService.isVerifyCodeCached(email, code);
    }

    @Override
    public void resendVerificationEmailCode(String email) {
        if (redisService.isResendCooldownActive(email)) {
            throw new IllegalStateException("You can only resend the verification code after a cooldown period.");
        }
        String code = generateVerificationCode();
        redisService.removeCachedVerifyCode(email);
        redisService.cacheVerifyCode(email, code);
        redisService.setResendCooldown(email, 90);
        mailService.sendMailAsync(new MailRequest(email, "Resend registration verification code", "Your verification code is: " + code));
    }

    private String generateVerificationCode() {
        Random random = new SecureRandom();
        int code = 100_000 + random.nextInt(900_000);
        return String.valueOf(code);
    }
}
