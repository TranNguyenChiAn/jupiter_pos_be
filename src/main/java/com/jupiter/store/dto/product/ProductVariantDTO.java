package com.jupiter.store.dto.product;

import com.jupiter.store.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private int price;
    private Integer quantity;
    private Long attributeId;
    private String attributeValue;
    private String imagePath;

    public ProductVariantDTO(ProductVariant productVariant) {
        this.price = productVariant.getPrice();
        this.quantity = productVariant.getQuantity();
        this.attributeId = productVariant.getAttributeId();
        this.attributeValue = productVariant.getAttributeValue();
        this.imagePath = productVariant.getImagePath();
    }
}
