package com.jupiter.store.module.category.dto;

import com.jupiter.store.module.category.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryQueryDTO implements IProductCategoryQueryDTO {
    private Integer id;
    private String categoryName;
    private Integer productId;

    public Category toCategory() {
        Category category = new Category();
        category.setId(this.id);
        category.setCategoryName(this.categoryName);
        return category;
    }
}
