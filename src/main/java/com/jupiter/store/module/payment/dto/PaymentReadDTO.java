package com.jupiter.store.module.payment.dto;

import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReadDTO {
    private Integer id;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private Long paid;
    private Long remaining;
    private LocalDateTime date;

    public PaymentReadDTO(Payment payment) {
        this.id = payment.getId();
        this.paymentMethod = payment.getPaymentMethod();
        this.status = payment.getStatus();
        this.paid = payment.getPaid();
        this.remaining = payment.getRemaining();
        this.date = payment.getDate();
    }
}
