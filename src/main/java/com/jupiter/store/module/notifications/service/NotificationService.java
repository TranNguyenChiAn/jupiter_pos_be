package com.jupiter.store.module.notifications.service;

import com.jupiter.store.common.config.TwilioConfig;
import com.jupiter.store.module.notifications.constant.NotificationType;
import com.jupiter.store.module.notifications.dto.NotificationDTO;
import com.jupiter.store.module.notifications.dto.NotificationMessage;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.repository.NotificationRepository;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendSms(String toNumber, Notification notification) {
        notification.setType(NotificationType.SMS);
        notificationRepository.save(notification);

        Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                notification.getBody()
        ).create();
    }

    @Async
    public void sendWebNotification(Notification stockAlertNotification) {
        stockAlertNotification.setType(NotificationType.WEB);
        notificationRepository.save(stockAlertNotification);

        messagingTemplate.convertAndSend("/topic/stock-alert", new NotificationMessage(
                stockAlertNotification.getTitle(),
                stockAlertNotification.getBody()
        ));
    }

    public List<Notification> getNotificationsByUserId(Integer userId, Integer page) {
        return notificationRepository.findByUserId(userId, page);
    }

    @Async
    public void sendNotification(NotificationDTO message) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setTitle(message.getTitle());
            notification.setBody(message.getBody());
            notification.setEntityType(message.getEntityType());
            notification.setEntityId(message.getEntityId());
            notification.setRead(false);
            notificationRepository.save(notification);

            CompletableFuture.runAsync(() -> {
                sendEmail(user.getEmail(), notification);
            });
            CompletableFuture.runAsync(() -> {
                sendWebNotification(notification);
            });
        }
    }

    @Async
    public void sendEmail(String toEmail, Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("$spring.mail.username");
        message.setTo(toEmail);
        message.setSubject(notification.getTitle());
        message.setText(notification.getBody());
        notification.setType(NotificationType.EMAIL);
        notificationRepository.save(notification);

        mailSender.send(message);
        System.out.println("Mail sent successfully!");
    }

    public Notification verifyOtp(Integer otp) {
        LocalDateTime validTime = LocalDateTime.now().minusMinutes(5);
        return notificationRepository.verifyByEntityId(otp, validTime)
                .orElseThrow(() -> new RuntimeException("OTP không hợp lệ hoặc đã hết hạn"));
    }

    public void updateUserToNull(Integer userId) {
        notificationRepository.updateUserToNull(userId);
    }
}