package com.web.meosocial.dto;

import com.web.meosocial.domain.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long recipientId;
    private Long senderId;
    private Integer type;
    private Long referenceId;
    private Integer referenceType;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;

    // Constructor to map from Notification entity to NotificationDto
    public NotificationDto(Notification notification) {
        if (notification != null) {
            this.id = notification.getId();
            this.recipientId = notification.getRecipient() != null ? notification.getRecipient().getId() : null;
            this.senderId = notification.getSenderId();
            this.type = notification.getType();
            this.referenceId = notification.getReferenceId();
            this.referenceType = notification.getReferenceType();
            this.content = notification.getContent();
            this.isRead = notification.getIsRead();
            this.createdAt = notification.getCreatedAt();
        }
    }
}
