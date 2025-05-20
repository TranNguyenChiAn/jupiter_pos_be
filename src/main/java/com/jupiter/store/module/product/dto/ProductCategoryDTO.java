package com.jupiter.store.module.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductCategoryDTO {
    Integer productId;
    List<Integer> categoryIds;
}
