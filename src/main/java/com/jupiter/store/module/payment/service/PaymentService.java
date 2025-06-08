package com.jupiter.store.module.payment.service;

import com.jupiter.store.common.config.VNPayConfig;
import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.order.model.Order;
import com.jupiter.store.module.order.repository.OrderRepository;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.model.Payment;
import com.jupiter.store.module.payment.repository.PaymentRepository;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {
    @Autowired
    private VNPayConfig config;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public String createPayment(
            Integer orderId,
            Long paid,
            PaymentMethod paymentMethod
    ) {
        try {
            switch (paymentMethod) {
                case VNPAY:
                    return createVNPayPayment(orderId, paid);
                case MOMO:
                    return ("Phương thức thanh toán Momo chưa được hỗ trợ");
                case TIEN_MAT:
                    return createCashPayment(orderId, paid);
                default:
                    return ("Phương thức thanh toán không hợp lệ");
            }
        } catch (Exception e) {
            return ("Lỗi thanh toán!");
        }
    }

    public String createVNPayPayment(Integer orderId, Long amount) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderType = "other";
        String vnp_IpAddr = "127.0.0.1"; // Có thể lấy từ HttpServletRequest
        String vnp_TmnCode = config.getVnpTmnCode();

        long paid = amount * 100; // VNPay yêu cầu đơn vị VNĐ * 100

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnp_Version);
        vnpParams.put("vnp_Command", vnp_Command);
        vnpParams.put("vnp_TmnCode", vnp_TmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(paid));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderId.toString());
        vnpParams.put("vnp_OrderInfo", "Order payment" +  orderId);
        vnpParams.put("vnp_OrderType", vnp_OrderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", config.getVnpReturnUrl());
        vnpParams.put("vnp_IpAddr", vnp_IpAddr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        vnpParams.put("vnp_CreateDate", now.format(formatter));

        // Sắp xếp theo thứ tự tăng dần
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if (!hashData.isEmpty()) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }

        // Ký SHA-256 với key bí mật
        String vnp_SecureHash = HmacSHA512(config.getVnpHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return config.getVnpPayUrl() + "?" + query.toString();
    }

    public String HmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] hash = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký", e);
        }
    }
    
    public String createCashPayment(Integer orderId, Long paid) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setDate(LocalDateTime.now());
        payment.setPaymentMethod(PaymentMethod.TIEN_MAT);
        payment.setPaid(paid);
        payment.setRemaining(order.getTotalAmount() - paid);
        payment.setStatus(PaymentStatus.THANH_TOAN_THANH_CONG);
        payment.setCreatedBy(order.getCreatedBy());
        paymentRepository.save(payment);
        return null;
    }
}
