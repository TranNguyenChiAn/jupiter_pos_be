package com.jupiter.store.module.category.resource;

import com.jupiter.store.module.category.dto.CategoryDTO;
import com.jupiter.store.module.category.dto.CategorySearchDTO;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {
    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public Category addCategory(@RequestBody CategoryDTO createCategoryDTO) {
        return categoryService.addCategory(createCategoryDTO.getCategoryName());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Category>> search(@RequestBody CategorySearchDTO request) {
        Page<Category> result = categoryService.search(
                request.getSearch(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/search/{categoryId}")
    public Category searchById(@PathVariable Integer categoryId) {
        return categoryService.searchById(categoryId);
    }

    @GetMapping("/search-by-name")
    public List<Category> searchByName(@RequestParam String categoryName) {
        return categoryService.searchByName(categoryName);
    }

    @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable Integer categoryId, @RequestBody String categoryName) {
        return categoryService.updateCategory(categoryId, categoryName.trim());
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}