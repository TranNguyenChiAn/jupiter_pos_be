package com.jupiter.store.dto.order;

import com.jupiter.store.constant.PaymentMethod;
import com.jupiter.store.constant.ShippingProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private Long cartId;
    private PaymentMethod paymentMethod;
    private ShippingInfoDTO shippingInfo;
} 