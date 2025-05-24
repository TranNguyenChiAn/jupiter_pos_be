package com.jupiter.store.module.product.model;

import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.model.ProductCategoryId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(ProductCategoryId.class)
public class ProductCategory {
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;
}