package com.jupiter.store.dto.product;

import com.jupiter.store.model.Product;
import com.jupiter.store.model.ProductCategory;
import com.jupiter.store.model.ProductImage;
import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.dto.AbstractAuditingDTO;
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
