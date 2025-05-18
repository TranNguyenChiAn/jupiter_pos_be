package com.jupiter.store.module.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductImage;
import com.jupiter.store.module.product.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductDTO {
    private Integer productId;
    private String productName;
    private String description;
    private List<Category> category;


    public GetProductDTO(Product product, List<Category> productCategory) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.category = productCategory;
    }
}
