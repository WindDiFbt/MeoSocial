package com.web.meosocial.domain.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Query("SELECT n FROM Notification n WHERE n.recipient.id = :recipientId")
    Page<Notification> findByRecipientId(@Param("recipientId") Long recipientId, Pageable pageable);

    Integer countByRecipientIdAndIsReadFalse(Long recipientId);

    List<Notification> findByRecipientIdAndIsReadFalse(Long recipientId);

    void deleteByRecipientId(Long recipientId);
}
