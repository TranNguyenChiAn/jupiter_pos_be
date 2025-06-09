package com.jupiter.store.module.notifications.service;

import com.jupiter.store.module.notifications.constant.NotificationType;
import com.jupiter.store.module.notifications.dto.NotificationDTO;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.repository.NotificationRepository;
import com.jupiter.store.module.user.dto.ChangePasswordDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void createSSEConnection() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

    }

    @Async
    public void sendWebNotification(Notification notification) {
        Notification msg = new Notification(notification);
        msg.setType(NotificationType.WEB);
        notificationRepository.save(msg);

        createSSEConnection();
        List<SseEmitter> deadEmitters = new java.util.ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(msg.getTitle()).data(msg.getBody()));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
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

            sendEmail(user.getEmail(), notification);
            sendWebNotification(notification);
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
    private void sendSms(Notification msg) {
        System.out.println("Gửi SMS: " + msg.getType());
    }

    public Notification verifyOtp(Integer otp) {
        LocalDateTime nowMinus5 = LocalDateTime.now().minusMinutes(5);
        return notificationRepository.verifyByEntityId(otp, nowMinus5)
                .orElseThrow(() -> new RuntimeException("OTP không hợp lệ hoặc đã hết hạn"));
    }
}