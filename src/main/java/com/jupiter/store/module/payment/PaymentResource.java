package com.jupiter.store.module.payment;

import com.jupiter.store.common.config.VNPayConfig;
import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.model.Payment;
import com.jupiter.store.module.payment.repository.PaymentRepository;
import com.jupiter.store.module.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentResource {
    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private VNPayConfig config;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/vnpay-return")
    public void vnpReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> fields = new HashMap<>();

        for (Enumeration<String> paramNames = request.getParameterNames(); paramNames.hasMoreElements();) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue != null && paramName.startsWith("vnp_")) {
                fields.put(paramName, paramValue);
            }
        }

        String receivedSecureHash = fields.remove("vnp_SecureHash");
        String secretKey = config.getVnpHashSecret();

        // Sắp xếp và tạo chuỗi hash giống lúc gửi
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = fields.get(fieldName);
            if (!hashData.isEmpty()) {
                hashData.append('&');
            }
            hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }

        String computedHash = paymentService.HmacSHA512(secretKey, hashData.toString());

        Integer orderId = Integer.parseInt(fields.get("vnp_TxnRef"));
        String responseCode = fields.get("vnp_ResponseCode");
        String transactionStatus = fields.get("vnp_TransactionStatus");
        Long paid = Long.parseLong(fields.get("vnp_Amount"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setDate(LocalDateTime.now());
        payment.setPaymentMethod(PaymentMethod.VNPAY);
        payment.setPaid(paid / 100); // VNPay trả về số tiền nhân với 100
        payment.setRemaining(order.getTotalAmount() - paid);  // Giả sử thanh toán đầy đủ

        if (computedHash.equals(receivedSecureHash)) {
            if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                // Giao dịch hợp lệ & thành công
                payment.setStatus(PaymentStatus.THANH_TOAN_THANH_CONG);
                response.sendRedirect("https://jupiterpos.vercel.app/admin/don-hang/");
            } else {
                payment.setStatus(PaymentStatus.THANH_TOAN_THAT_BAI);
                log.debug("Lỗi: " + responseCode);
                response.sendRedirect("https://jupiterpos.vercel.app/admin/don-hang/");
            }
        } else {
            payment.setStatus(PaymentStatus.THANH_TOAN_CO_THE_BI_GIA_MAO);
            response.sendRedirect("https://jupiterpos.vercel.app/admin/don-hang/");
        }
        payment.setCreatedBy(order.getCreatedBy());
        paymentRepository.save(payment);
    }
}
