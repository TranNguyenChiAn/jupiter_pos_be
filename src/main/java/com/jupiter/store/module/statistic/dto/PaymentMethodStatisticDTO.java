package com.jupiter.store.module.statistic.dto;

import com.jupiter.store.module.payment.constant.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodStatisticDTO {
    private PaymentMethod paymentMethod;
    private Integer transactionCount;
    private Long totalAmount;
}
