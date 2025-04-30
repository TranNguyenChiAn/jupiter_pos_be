package com.jupiter.store.dto.product;

import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.model.ProductVariantAttrValue;
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
    private int price;
    private Integer quantity;
    private List<ProductVariantAttrValueDto> attrAndValues;
    private String imagePath;
}
