package com.jupiter.store.module.category.dto;

import com.jupiter.store.module.category.model.Category;

public interface IProductCategoryQueryDTO {
    Integer getId();

    String getCategoryName();

    Integer getProductId();

    default Category toCategory() {
        Category category = new Category();
        category.setId(getId());
        category.setCategoryName(getCategoryName());
        return category;
    }
}
