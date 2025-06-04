package com.jupiter.store.module.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailCreateDTO {
    private Integer productVariantId;
    private Long price;
    private Integer soldQuantity;
    private Long soldPrice;
}
