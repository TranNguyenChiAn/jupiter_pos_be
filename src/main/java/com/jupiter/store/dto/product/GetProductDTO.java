package com.jupiter.store.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.domain.*;
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
    private Long productId;
    private String name;
    private String description;
    private String material;
    private List<Category> category;
    private List<ProductImage> imagePath;
    private List<ProductVariant> variant;


    public GetProductDTO(Product product, List<Category> productCategory, List<ProductVariant> productVariants, List<ProductImage> productImages) {
        this.productId = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.material = product.getMaterial();
        this.category = productCategory;
        this.imagePath = productImages;
        this.variant = productVariants;
    }
}
