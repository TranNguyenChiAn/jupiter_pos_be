package com.jupiter.store.dto;

import com.jupiter.store.domain.Category;
import com.jupiter.store.domain.ProductCategory;
import com.jupiter.store.domain.ProductImage;
import com.jupiter.store.domain.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductDTO {
    private String name;
    private String description;
    private String material;
    private List<Long> categoryId;
    private List<String> imagePath;
    private List<ProductVariantDTO> variants;
}
