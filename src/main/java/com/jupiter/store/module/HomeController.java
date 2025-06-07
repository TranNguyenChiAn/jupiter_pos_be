package com.jupiter.store.module;

import com.jupiter.store.common.utils.FakeDataGenerator;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.notifications.constant.NotificationEntityType;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {
    @Autowired
    private NotificationService notificationService;

    private final FakeDataGenerator fakeDataGenerator;

    public HomeController(FakeDataGenerator fakeDataGenerator) {
        this.fakeDataGenerator = fakeDataGenerator;
    }

    @GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping("/api/fake-data")
    public void generateFakeData() {
        fakeDataGenerator.fake();
    }

    @GetMapping("api/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/payment-result")
    public void handlePaymentResult(@RequestParam String status, @RequestParam Integer orderId) {
        Notification notification = new Notification();
        notification.setTitle("Kết quả thanh toán");
        if(status.equals("success")) {
            notification.setBody("Thanh toán thành công cho đơn hàng " + orderId);
        } else if(status.equals("fail")) {
            notification.setBody("Thanh toán thất bại cho đơn hàng " + orderId);
        } else {
            notification.setBody("Thanh toán có thể bị giả mạo cho đơn hàng " + orderId);
        }
        notification.setEntityType(NotificationEntityType.PAYMENT);
        notification.setRead(false);
        notification.setUserId(SecurityUtils.getCurrentUserId());
        notificationService.sendWebNotification(notification);
    }
}
