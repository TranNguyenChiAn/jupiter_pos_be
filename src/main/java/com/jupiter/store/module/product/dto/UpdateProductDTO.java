package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.module.product.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductDTO {
    private String productName;
    private String description;
    private ProductStatus status;
}
