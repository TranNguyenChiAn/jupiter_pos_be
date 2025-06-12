package com.jupiter.store.module.payment.dto;

import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentOrderDTO {
    private Integer orderId;
    private Long paid;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}
