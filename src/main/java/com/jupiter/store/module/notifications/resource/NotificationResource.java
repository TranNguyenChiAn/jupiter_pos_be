package com.jupiter.store.module.notifications.resource;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationResource {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/search")
    public List<Notification> getNotificationsByUserId(@RequestParam(defaultValue = "0") Integer page) {
        return notificationService.getNotificationsByUserId(SecurityUtils.getCurrentUserId(), page);
    }
}
