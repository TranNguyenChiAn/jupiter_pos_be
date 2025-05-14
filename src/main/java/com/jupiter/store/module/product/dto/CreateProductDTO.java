package com.jupiter.store.module.product.dto;

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
    private List<Integer> categoryId;
    private List<String> imagePath;
}
