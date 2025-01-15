package com.jupiter.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartId;
    private Long productVariantId;
    private Long quantity;
    private int totalAmount;
}
