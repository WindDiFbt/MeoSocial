package com.web.meosocial.domain.notification.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.notification.Notification;
import com.web.meosocial.domain.notification.NotificationDto;
import com.web.meosocial.domain.notification.NotificationRepository;
import com.web.meosocial.domain.notification.service.NotificationService;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.repository.UserRepository;
import com.web.meosocial.exception.UnauthorizedException;
import com.web.meosocial.payload.response.ApiResponse;
import com.web.meosocial.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ApiResponseUtils apiResponseUtils;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createU2UNotification(Long recipientId, Long senderId, Integer type, String content) {
        if (recipientId.equals(senderId)) {
            return;
        }
        User recipient = getUserById(recipientId);
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setRecipient(recipient);
        notification.setSenderId(senderId);
        notification.setType(type);
        notification.setContent(content);
        notification.setReferenceType(Enums.ReferenceType.USER_TO_USER.getValue());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        sendRealTimeNotification(notification);
    }

    @Override
    public void createS2UNotification(Long recipientId, Integer type, String content) {
        User recipient = getUserById(recipientId);
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setContent(content);
        notification.setReferenceType(Enums.ReferenceType.SYSTEM_TO_USER.getValue());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        sendRealTimeNotification(notification);
    }

    @Override
    public ApiResponse<List<NotificationDto>> getNotificationsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationRepository.findByRecipientId(userId, pageable);
        return apiResponseUtils.success(notifications.stream().map(NotificationDto::new).toList(), "Notifications found!");
    }

    @Override
    public ApiResponse<Integer> countUnreadNotifications(Long userId) {
        return apiResponseUtils.success(notificationRepository.countByRecipientIdAndIsReadFalse(userId), "Notifications found!");
    }

    @Override
    public ApiResponse<Void> markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
        return apiResponseUtils.success(null, "Notification marked as read!");
    }

    @Override
    public ApiResponse<Void> markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByRecipientIdAndIsReadFalse(userId);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
        return apiResponseUtils.success(null, "All Notification marked as read!");
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteNotification(Long userId, String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("Notification not found!"));
        if (!notification.getRecipient().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this notification!");
        }
        notificationRepository.deleteById(notificationId);
        return apiResponseUtils.success(null, "Notification deleted!");
    }

    @Override
    public ApiResponse<Void> deleteAllNotificationsByUser(Long userId) {
        notificationRepository.deleteByRecipientId(userId);
        return apiResponseUtils.success(null, "All Notifications deleted!");
    }

    @Override
    public void sendRealTimeNotification(Notification notification) {
        NotificationDto notificationDto = new NotificationDto(notification);
        simpMessagingTemplate.convertAndSend("/meoSocial/notification" + notification.getRecipient().getId(), notificationDto);
    }

    private User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getUserStatus().equals(Enums.UserStatus.NOT_AVAILABLE.getValue())) {
            throw new IllegalArgumentException("User not found or not available.");
        }
        return user;
    }
}
