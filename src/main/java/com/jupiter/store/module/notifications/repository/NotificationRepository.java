package com.jupiter.store.module.notifications.repository;

import com.jupiter.store.module.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT * FROM notifications n WHERE (:userId IS NULL OR n.user_id = :userId) AND n.entity_type != 'PASSWORD_RESET' " +
            "ORDER BY n.date DESC " +
            "OFFSET :page * 10 LIMIT 10 ", nativeQuery = true)
    List<Notification> findByUserId(@Param("userId") Integer userId, @Param("page") Integer page);


    @Query(value = "SELECT * FROM notifications n " +
            "WHERE n.entity_type = 'PASSWORD_RESET' " +
            "AND n.entity_id = :otp " +
            "AND n.date >= :validTime ", nativeQuery = true)
    Optional<Notification> verifyByEntityId(@Param("otp") Integer otp, @Param("validTime") LocalDateTime validTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE notifications n SET user_id = NULL WHERE n.user_id = :userId", nativeQuery = true)
    void updateUserToNull(@Param("userId") Integer userId);
}