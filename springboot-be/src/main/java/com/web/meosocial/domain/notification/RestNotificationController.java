package com.web.meosocial.domain.notification;

import com.web.meosocial.auth.AuthUtils;
import com.web.meosocial.domain.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class RestNotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthUtils authUtils;

    @GetMapping()
    public ResponseEntity<?> getNotification(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(notificationService.getNotificationsByUser(authUtils.getCurrentUserId(), page, size));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount() {
        return ResponseEntity.ok().body(notificationService.countUnreadNotifications(authUtils.getCurrentUserId()));
    }

    @PatchMapping("/mark/{notificationId}")
    public ResponseEntity<?> markNotification(@PathVariable String notificationId) {
        return ResponseEntity.ok().body(notificationService.markAsRead(notificationId));
    }

    @PatchMapping("/mark-all")
    public ResponseEntity<?> markAllNotifications() {
        return ResponseEntity.ok().body(notificationService.markAllAsRead(authUtils.getCurrentUserId()));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable String notificationId) {
        return ResponseEntity.ok().body(notificationService.deleteNotification(authUtils.getCurrentUserId(), notificationId));
    }
}
