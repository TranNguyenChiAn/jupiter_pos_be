package com.jupiter.store.module.payment;

import com.jupiter.store.module.payment.dto.UpdatePaymentOrderDTO;
import com.jupiter.store.module.payment.model.Payment;
import com.jupiter.store.module.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentResource {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/generate-qr")
    public ResponseEntity<Map<String, String>> generateQR(@RequestParam Long amount) {
        String qrUrl = paymentService.generateQRUrl(amount, "Thanh toán đơn hàng");
        Map<String, String> response = new HashMap<>();
        response.put("qrUrl", qrUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-for-order")
    public ResponseEntity<Payment> updatePayment(@RequestBody UpdatePaymentOrderDTO updatePaymentOrderDTO) {
        Payment updatedPayment = paymentService.updatePayment(
                updatePaymentOrderDTO.getOrderId(),
                updatePaymentOrderDTO.getPaid(),
                updatePaymentOrderDTO.getPaymentMethod(),
                updatePaymentOrderDTO.getPaymentStatus()
        );
        return ResponseEntity.ok(updatedPayment);
    }
}
