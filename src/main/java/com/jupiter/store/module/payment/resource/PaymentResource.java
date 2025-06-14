package com.jupiter.store.module.payment.resource;

import com.jupiter.store.module.payment.dto.CreatePaymentOrderDTO;
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

    @PostMapping("/create-for-order")
    public ResponseEntity<Payment> createPayment(@RequestBody CreatePaymentOrderDTO createPaymentOrderDTO) {
        Payment newPayment = paymentService.createMorePaymentForOrder(
                createPaymentOrderDTO.getOrderId(),
                createPaymentOrderDTO.getPaid(),
                createPaymentOrderDTO.getPaymentMethod()
        );
        return ResponseEntity.ok(newPayment);
    }

    @PutMapping("/update-for-order")
    public ResponseEntity<Payment> Payment(@RequestBody UpdatePaymentOrderDTO updatePaymentOrderDTO) {
        Payment updatedPayment = paymentService.updatePayment(
                updatePaymentOrderDTO.getPaymentId(),
                updatePaymentOrderDTO.getPaymentMethod()
        );
        return ResponseEntity.ok(updatedPayment);
    }
}
