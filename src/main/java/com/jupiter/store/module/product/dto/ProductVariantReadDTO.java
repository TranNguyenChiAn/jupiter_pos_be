package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.common.model.AbstractAuditingEntity;
import com.jupiter.store.module.product.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVariantReadDTO extends AbstractAuditingEntity implements Serializable {
    private Integer id;
    private ProductReadDTO product;
    private Long costPrice;
    private Long price;
    private Integer quantity;
    private Integer unitId;
    private String sku;
    private String barcode;
    private LocalDateTime expiryDate;
    private List<ProductVariantAttrValueSimpleReadDTO> attrValues;

    public ProductVariantReadDTO(ProductVariant productVariant) {
        this.id = productVariant.getId();
        this.costPrice = productVariant.getCostPrice();
        this.price = productVariant.getPrice();
        this.quantity = productVariant.getQuantity();
        this.unitId = productVariant.getUnitId();
        this.sku = productVariant.getSku();
        this.barcode = productVariant.getBarcode();
        this.expiryDate = productVariant.getExpiryDate();
    }
}
