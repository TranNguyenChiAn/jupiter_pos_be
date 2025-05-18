package com.jupiter.store.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductVariantDTO {
    private Long costPrice;
    private Long price;
    private Integer quantity;
    private Integer unitId;
    private String sku;
    private String barcode;
    private LocalDateTime expiryDate;
    private List<ProductVariantAttrValueDto> attrAndValues;
    private List<String> imagePaths;
}
