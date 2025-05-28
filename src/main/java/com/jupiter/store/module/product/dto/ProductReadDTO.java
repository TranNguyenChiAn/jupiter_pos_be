package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.common.model.AbstractAuditingEntity;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.product.constant.ProductStatus;
import com.jupiter.store.module.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReadDTO extends AbstractAuditingEntity implements Serializable {
    private Integer productId;
    private String productName;
    private String description;
    private ProductStatus status;
    private List<Category> category;

    public ProductReadDTO(Product product, List<Category> productCategory) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.status = product.getStatus();
        this.category = productCategory;
        this.setCreatedBy(product.getCreatedBy());
        this.setCreatedDate(product.getCreatedDate());
        this.setLastModifiedBy(product.getLastModifiedBy());
        this.setLastModifiedDate(product.getLastModifiedDate());
    }
}
