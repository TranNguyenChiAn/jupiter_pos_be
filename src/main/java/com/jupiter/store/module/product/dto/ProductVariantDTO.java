package com.jupiter.store.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private Long price;
    private Integer quantity;
    private List<ProductVariantAttrValueDto> attrAndValues;
    private String imagePath;
}
