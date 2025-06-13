package com.jupiter.store.module.payment.dto;

import com.jupiter.store.module.payment.constant.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentOrderDTO {
    private Integer orderId;
    private Long paid;
    private PaymentMethod paymentMethod;
}
