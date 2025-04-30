package com.jupiter.store.dto.order;

import com.jupiter.store.constant.ShippingProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfoDTO {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private ShippingProvider shippingProvider;
    private Integer shippingCost;
}
