package com.web.meosocial.domain.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mailFrom}")
    private String mailFrom;

    @Async
    public void sendMailAsync(MailRequest request) {
        sendMail(request);
    }

    @Retryable(
            retryFor = {MailSendException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendMail(MailRequest request) {
        if (request.getTo() == null || request.getTo().isBlank()) {
            throw new IllegalArgumentException("Recipient email is missing.");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        mailSender.send(message);
    }

    @Recover
    public void recover(MailSendException e, MailRequest request) {
        log.warn("Mail send error: {}, Request: {}", e, request);
    }
}
