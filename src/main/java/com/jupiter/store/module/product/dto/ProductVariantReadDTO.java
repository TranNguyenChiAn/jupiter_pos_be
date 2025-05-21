package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVariantReadDTO {
    private Integer id;
    private ProductReadDTO product;
    private Long costPrice;
    private Long price;
    private Integer quantity;
    private Integer unitId;
    private String sku;
    private String barcode;
    private LocalDateTime expiryDate;
}
