package com.jupiter.store.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantAttrValueSimpleReadDTO {
    private Integer attrId;
    private String attrName;
    private String attrValue;
    private Integer unitId;
    private String unitName;
}
