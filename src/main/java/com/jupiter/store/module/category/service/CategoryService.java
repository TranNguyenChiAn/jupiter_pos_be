package com.jupiter.store.module.category.service;

import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.repository.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final HelperUtils helperUtils;

    public CategoryService(CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, HelperUtils helperUtils) {
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.helperUtils = helperUtils;
    }

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Page<Category> search(String search, Integer page, Integer size, String sortBy, String sortDirection) {
        helperUtils.validatePageAndSize(page, size);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        search = helperUtils.normalizeSearch(search);
        return categoryRepository.search(search, pageable);
    }

    public Category addCategory(String categoryName) {
        if (categoryRepository.existsByCategoryName(categoryName.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục " + categoryName + " đã tồn tại!");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        Sort sort = Sort.by(Sort.Direction.fromString("DESC"), "id");
        Pageable pageable = PageRequest.of(0, 99999, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return page.getContent();
    }

    public Category searchById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
    }

    public Category updateCategory(Integer categoryId, String categoryName) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw new IllegalArgumentException("Tên danh mục " + categoryName + " đã tồn tại!");
        }
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
        productCategoryRepository.deleteByCategoryId(categoryId);
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