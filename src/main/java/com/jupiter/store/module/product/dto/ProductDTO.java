package com.jupiter.store.module.product.dto;

import com.jupiter.store.common.dto.AbstractAuditingDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductCategory;
import com.jupiter.store.module.product.model.ProductImage;
import com.jupiter.store.module.product.model.ProductVariant;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    private String name;

    private String description;

    private List<ProductCategory> categoryId;

    private List<ProductImage> imagePath;

    private List<ProductVariant> variants;


    public ProductDTO(Product product, List<ProductCategory> productCategory, List<ProductVariant> variants, List<ProductImage> productImages) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.categoryId = productCategory;
        this.imagePath = productImages;
        this.variants = variants;
    }
}
