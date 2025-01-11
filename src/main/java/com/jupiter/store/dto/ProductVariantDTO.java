package com.jupiter.store.dto;

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
}
