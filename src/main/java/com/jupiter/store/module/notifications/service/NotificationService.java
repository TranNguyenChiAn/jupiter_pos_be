package com.jupiter.store.module.notifications.service;

import com.jupiter.store.module.notifications.dto.NotificationDTO;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.repository.NotificationRepository;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;


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

            sendEmail(user.getEmail(), message.getTitle(), message.getBody());
        }
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("$spring.mail.username");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Mail sent successfully...");
    }


    private void sendSms(Notification msg) {
        System.out.println("Gửi SMS: " + msg.getType());
    }

    private void sendWebNotification(Notification msg) {
        System.out.println("Web Notification: " + msg.getType());
    }
}