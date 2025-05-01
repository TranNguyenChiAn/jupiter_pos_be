package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.payment.constant.PaymentMethod;
import com.jupiter.store.module.payment.constant.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderDTO {
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    ShippingInfoDTO shippingInfo;
}
