package com.jupiter.store.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.store.domain.ProductImage;
import com.jupiter.store.domain.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductDTO {
    private String name;
    private String description;
    private String material;
    private List<String> imagePaths;
}
