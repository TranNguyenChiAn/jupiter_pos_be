package com.jupiter.store.dto.product;

import com.jupiter.store.domain.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private int price;
    private Integer quantity;
    private String color;
    private Long sizeId;
    private String imagePath;

    public ProductVariantDTO(ProductVariant productVariant) {
        this.price = productVariant.getPrice();
        this.quantity = productVariant.getQuantity();
        this.color = productVariant.getColor();
        this.sizeId = productVariant.getSizeId();
        this.imagePath = productVariant.getImagePath();
    }
}
