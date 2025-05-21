package com.jupiter.store.module.product.dto;

import com.jupiter.store.module.product.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithVariantsReadDTO {
    private ProductReadDTO product;
    private List<ProductVariantReadDTO> variants;
}
