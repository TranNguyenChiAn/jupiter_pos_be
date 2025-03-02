package com.jupiter.store.web.rest;

import com.jupiter.store.constant.RoleBase;
import com.jupiter.store.domain.Category;
import com.jupiter.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void addCategory(String name) {
        categoryService.addCategory(name);
    }

    @GetMapping("/search")
    public List<Category> search() {
        return categoryService.search();
    }

    @GetMapping("/search/{id}")
    public Category searchById(Long id) {
        return categoryService.searchById(id);
    }

    @PutMapping("/update")
    public void updateCategory(@RequestParam Long categoryId, @RequestParam String name) {
        categoryService.updateCategory(categoryId, name);
    }

    @DeleteMapping("/delete")
    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }
}
