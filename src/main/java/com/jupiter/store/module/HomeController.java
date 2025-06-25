package com.jupiter.store.module;

import com.jupiter.store.common.utils.FakeDataGenerator;
import com.jupiter.store.module.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {
    private final FakeDataGenerator fakeDataGenerator;
    @Autowired
    private NotificationService notificationService;

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
}
