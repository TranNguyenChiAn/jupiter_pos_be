package com.jupiter.store.dto.product;

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
    private List<Long> categoryId;
    private List<String> imagePath;
    private List<ProductVariantDTO> variants;
}
