package com.jupiter.store.module.notifications.dto;

import com.jupiter.store.module.notifications.constant.NotificationEntityType;
import com.jupiter.store.module.notifications.constant.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NotificationDTO {
    private String title;
    private String body;
    private NotificationEntityType entityType;
    private Integer entityId;


    public NotificationDTO(String title, String body, NotificationEntityType entityType, Integer entityId) {
        this.title = title;
        this.body = body;
        this.entityType = entityType;
        this.entityId = entityId;
    }
}
