package com.web.meosocial.domain.notification;

import com.web.meosocial.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "reference_type")
    private Integer referenceType;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor to convert NotificationDto to Notification entity
    public Notification(NotificationDto notificationDto) {
        if (notificationDto != null) {
            this.id = notificationDto.getId();
            this.senderId = notificationDto.getSenderId();
            this.type = notificationDto.getType();
            this.referenceType = notificationDto.getReferenceType();
            this.content = notificationDto.getContent();
            this.isRead = notificationDto.getIsRead();
            this.createdAt = notificationDto.getCreatedAt();
            if (notificationDto.getRecipientId() != null) {
                this.recipient = new User();
                this.recipient.setId(notificationDto.getRecipientId());
            }
        }
    }
}