package com.jupiter.store.module.payment.dto;

import com.jupiter.store.module.payment.constant.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentOrderDTO {
    private Integer paymentId;
    private PaymentMethod paymentMethod;
}
