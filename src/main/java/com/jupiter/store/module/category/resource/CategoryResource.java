package com.jupiter.store.module.category.resource;

import com.jupiter.store.module.category.dto.CategoryDTO;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.service.CategoryService;
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
        return categoryService.addCategory(createCategoryDTO.getName());
    }

    @GetMapping("/search")
    public List<Category> search() {
        return categoryService.search();
    }

    @GetMapping("/search/{categoryId}")
    public Category searchById(@PathVariable Integer categoryId) {
        return categoryService.searchById(categoryId);
    }

    @GetMapping("/search-by-name")
    public List<Category> searchByName(@RequestParam String categoryName) {
        return categoryService.searchByName(categoryName);
    }

    @PutMapping("/update/{categoryId}")
    public void updateCategory(@PathVariable Integer categoryId, @RequestParam String name) {
        categoryService.updateCategory(categoryId, name);
    }

    @DeleteMapping("/delete")
    public void deleteCategory(Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}