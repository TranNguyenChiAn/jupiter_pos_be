package com.jupiter.store.module.notifications.repository;

import com.jupiter.store.module.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT * FROM notifications n WHERE n.user_id = :userId AND n.entity_type != 'PASSWORD_RESET' " +
                    "ORDER BY n.date DESC " +
                    "OFFSET :page * 10 LIMIT 10 ", nativeQuery = true)
    List<Notification> findByUserId(@Param("userId") Integer userId, @Param("page") Integer page);


    @Query(value = "SELECT * FROM notifications n " +
                    "WHERE n.entity_type = 'PASSWORD_RESET' " +
                    "AND n.entity_id = :otp " +
                    "AND n.date >= NOW() - INTERVAL '7 hour' - INTERVAL '5 minute' ", nativeQuery = true)
    Optional<Notification> verifyByEntityId(
            @Param("otp") Integer otp,
            @Param("nowMinus5")LocalDateTime nowMinus5);
}