package com.web.meosocial.domain.notification.service;

import com.web.meosocial.domain.notification.Notification;
import com.web.meosocial.domain.notification.NotificationDto;
import com.web.meosocial.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    void createU2UNotification(Long recipientId, Long senderId, Integer type, String content);

    void createS2UNotification(Long recipientId, Integer type, String content);

    ApiResponse<List<NotificationDto>> getNotificationsByUser(Long userId, int page, int size);

    ApiResponse<Integer> countUnreadNotifications(Long userId);

    ApiResponse<Void> markAsRead(String notificationId);

    ApiResponse<Void> markAllAsRead(Long userId);

    ApiResponse<Void> deleteNotification(Long userId, String notificationId);

    ApiResponse<Void> deleteAllNotificationsByUser(Long userId);

    void sendRealTimeNotification(Notification notification);
}
