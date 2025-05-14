package com.jupiter.store.module.notifications.resource;

import com.jupiter.store.module.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationResource {
    @Autowired
    private NotificationService notificationService;
}
