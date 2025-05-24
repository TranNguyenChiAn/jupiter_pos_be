package com.jupiter.store.module.category.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Category addCategory(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục " + categoryName + " đã tồn tại!");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    public List<Category> search() {
        return categoryRepository.findAll();
    }

    public Category searchById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
    }

    public void updateCategory(Integer categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
        if (categoryRepository.existsByCategoryName(name)) {
            throw new IllegalArgumentException("Tên danh mục " + name + " đã tồn tại!");
        }
        category.setCategoryName(name);
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
        categoryRepository.delete(category);
    }

    public List<Category> searchByName(String categoryName) {
        List<Category> categories = categoryRepository.findByCategoryName(categoryName.toLowerCase());
        if (categories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục nào với tên: " + categoryName);
        }
        return categories;
    }
}