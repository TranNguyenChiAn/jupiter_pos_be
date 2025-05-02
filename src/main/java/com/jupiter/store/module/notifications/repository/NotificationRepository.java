package com.jupiter.store.module.notifications.repository;

import com.jupiter.store.module.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT * FROM notifications n WHERE n.user_id = :userId", nativeQuery = true)
    List<Notification> findByUserId(@Param("userId") Long userId);
}