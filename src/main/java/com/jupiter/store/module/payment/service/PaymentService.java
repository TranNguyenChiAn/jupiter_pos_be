package com.jupiter.store.module.payment.service;

import com.jupiter.store.common.config.VietQRConfig;
import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.dto.PaymentReadDTO;
import com.jupiter.store.module.payment.model.Payment;
import com.jupiter.store.module.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private VietQRConfig vietQRConfig;

    public void createPayment(
            Integer orderId,
            Long paid,
            PaymentMethod paymentMethod
    ) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));

            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setDate(LocalDateTime.now());
            payment.setPaymentMethod(paymentMethod);
            payment.setPaid(paid);
            payment.setRemaining(order.getTotalAmount() - paid);
            payment.setStatus(PaymentStatus.THANH_TOAN_THANH_CONG);
            payment.setCreatedBy(order.getCreatedBy());
            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo thanh toán: " + e.getMessage(), e);
        }
    }

    public String generateQRUrl(Long amount, String content) {
        String baseUrl = "https://img.vietqr.io/image";
        String qrPath = String.format("%s-%s-compact2.png",
                vietQRConfig.getVietQrBankCode(), vietQRConfig.getVietQrBankAccount());

        return String.format("%s/%s?amount=%d&addInfo=%s&accountName=%s",
                baseUrl,
                qrPath,
                amount,
                UriUtils.encode(content, StandardCharsets.UTF_8),
                UriUtils.encode(vietQRConfig.getVietQrUserBankName(), StandardCharsets.UTF_8)
        );
    }

    public List<PaymentReadDTO> getPaymentsByOrderId(Integer id) {
        List<Payment> payments = paymentRepository.findByOrderId(id);
        return payments.stream()
                .map(PaymentReadDTO::new)
                .collect(Collectors.toList());
    }
}
