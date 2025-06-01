package com.web.meosocial.domain.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailServiceTest {

    @TestConfiguration
    static class MailServiceTestConfig {
        @Bean
        @Primary
        public JavaMailSender javaMailSender() {
            return mock(JavaMailSender.class);
        }
    }

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailService mailService;

    private MailRequest validMailRequest;
    private final String TEST_MAIL_FROM = "test@example.com";

    @BeforeEach
    void setUp() {
        // Reset the mock before each test
        reset(mailSender);

        // Set up the mail from property using ReflectionTestUtils
        ReflectionTestUtils.setField(mailService, "mailFrom", TEST_MAIL_FROM);

        // Create a valid mail request for testing
        validMailRequest = new MailRequest();
        validMailRequest.setTo("recipient@example.com");
        validMailRequest.setSubject("Test Subject");
        validMailRequest.setText("Test Message");
    }

    @Test
    void sendMail_withValidRequest_shouldSendEmail() {
        // Arrange
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        mailService.sendMail(validMailRequest);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertEquals(TEST_MAIL_FROM, capturedMessage.getFrom());
        assertEquals(validMailRequest.getTo(), capturedMessage.getTo()[0]);
        assertEquals(validMailRequest.getSubject(), capturedMessage.getSubject());
        assertEquals(validMailRequest.getText(), capturedMessage.getText());
    }

    @Test
    void sendMail_withNullRecipient_shouldThrowException() {
        // Arrange
        MailRequest invalidRequest = new MailRequest();
        invalidRequest.setSubject("Test Subject");
        invalidRequest.setText("Test Message");
        // To is null

        // Act & Assert
        Exception exception = assertThrows(
            Exception.class,
            () -> mailService.sendMail(invalidRequest)
        );

        // Check if the exception or its cause contains the expected message
        String errorMessage = "Recipient email is missing.";
        boolean hasExpectedMessage = false;

        Throwable current = exception;
        while (current != null) {
            if (current instanceof IllegalArgumentException && 
                errorMessage.equals(current.getMessage())) {
                hasExpectedMessage = true;
                break;
            }
            current = current.getCause();
        }

        assertTrue(hasExpectedMessage, "Exception chain should contain IllegalArgumentException with message: " + errorMessage);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMail_withBlankRecipient_shouldThrowException() {
        // Arrange
        MailRequest invalidRequest = new MailRequest();
        invalidRequest.setTo("");  // Blank recipient
        invalidRequest.setSubject("Test Subject");
        invalidRequest.setText("Test Message");

        // Act & Assert
        Exception exception = assertThrows(
            Exception.class,
            () -> mailService.sendMail(invalidRequest)
        );

        // Check if the exception or its cause contains the expected message
        String errorMessage = "Recipient email is missing.";
        boolean hasExpectedMessage = false;

        Throwable current = exception;
        while (current != null) {
            if (current instanceof IllegalArgumentException && 
                errorMessage.equals(current.getMessage())) {
                hasExpectedMessage = true;
                break;
            }
            current = current.getCause();
        }

        assertTrue(hasExpectedMessage, "Exception chain should contain IllegalArgumentException with message: " + errorMessage);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMail_withMailSendException_shouldRetry() {
        // Arrange
        doThrow(new MailSendException("Test exception"))
            .doThrow(new MailSendException("Test exception"))
            .doNothing()
            .when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        mailService.sendMail(validMailRequest);

        // Assert
        // Verify that send was called 3 times (2 failures + 1 success)
        verify(mailSender, times(3)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendMailAsync_shouldCallSendMail() {
        // This test is limited because we can't easily test async behavior in a unit test
        // We can only verify that sendMail is called with the correct parameters

        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        mailService.sendMailAsync(validMailRequest);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void recover_shouldHandleMailSendException() {
        // This test verifies that the recover method doesn't throw any exceptions
        // Since the method only logs the error, we can't easily test the logging behavior

        // Arrange
        MailSendException exception = new MailSendException("Test exception");

        // Act & Assert
        assertDoesNotThrow(() -> mailService.recover(exception, validMailRequest));
    }
}
