package com.jupiter.store.dto.cart;

import com.jupiter.store.dto.product.ProductVariantAttrValueDto;
import com.jupiter.store.dto.product.ProductVariantDTO;
import com.jupiter.store.model.Product;
import com.jupiter.store.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productName;
    private ProductVariantDTO productVariant;
    private String imagePath;
    private int quantity;
    private int totalAmount;
    private boolean isSelected;
}
