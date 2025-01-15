package com.jupiter.store.dto;

import com.jupiter.store.domain.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDTO {
    private Long cartId;
    private Long productVariantId;
    private int quantity;
}
