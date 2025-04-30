package com.jupiter.store.dto.order;

import com.jupiter.store.constant.PaymentMethod;
import com.jupiter.store.constant.PaymentStatus;

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
