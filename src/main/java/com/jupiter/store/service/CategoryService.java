package com.jupiter.store.service;

import com.jupiter.store.domain.Category;
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

    public void addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setCreatedBy(3481888888888888L);
        category.setCreatedDate(LocalDateTime.now());
        categoryRepository.save(category);
    }

    public List<Category> search() {
        return categoryRepository.findAll();
    }

    public Category searchById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void updateCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
