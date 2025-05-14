package com.jupiter.store.module.category.service;

import com.jupiter.store.common.exception.CustomException;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Category addCategory(String name) {
        Category category = new Category();
        category.setCategoryName(name);
        return categoryRepository.save(category);
    }

    public List<Category> search() {
        return categoryRepository.findAll();
    }

    public Category searchById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void updateCategory(Integer categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException("Category not found", HttpStatus.NOT_FOUND));
        category.setCategoryName(name);
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}