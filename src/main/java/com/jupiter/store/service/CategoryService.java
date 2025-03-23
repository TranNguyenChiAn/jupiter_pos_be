package com.jupiter.store.service;

import com.jupiter.store.model.Category;
import com.jupiter.store.repository.CategoryRepository;
import com.jupiter.store.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    public Category addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setCreatedBy(currentUserId());
        category.setCreatedDate(LocalDateTime.now());
        return categoryRepository.save(category);
    }
    public List<Category> search() {
        return categoryRepository.findAll();
    }
    public Category searchById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public void updateCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        category.setName(name);
        category.setLastModifiedBy(currentUserId());
        categoryRepository.save(category);
    }
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}