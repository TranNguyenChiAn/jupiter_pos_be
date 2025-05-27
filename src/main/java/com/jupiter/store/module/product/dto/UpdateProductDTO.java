package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.module.product.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductDTO {
    private String productName;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private List<Integer> categoryIds;
    private List<CreateProductVariantDTO> variants;
}
