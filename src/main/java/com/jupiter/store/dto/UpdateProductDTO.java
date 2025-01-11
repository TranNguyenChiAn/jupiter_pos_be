package com.jupiter.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateProductDTO {
    private String name;
    private String description;
    private String material;
    private List<Long> categoryId;
    private List<String> imagePath;
    private List<ProductVariantDTO> variants;
}
