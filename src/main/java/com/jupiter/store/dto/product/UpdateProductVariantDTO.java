package com.jupiter.store.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductVariantDTO {
    private Long id;
    private int price;
    private Integer quantity;
    private String color;
    private Long sizeId;
    private String imagePath;
}
