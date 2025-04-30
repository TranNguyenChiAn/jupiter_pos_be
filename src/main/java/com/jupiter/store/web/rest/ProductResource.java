package com.jupiter.store.web.rest;

import com.jupiter.store.constant.RoleBase;
import com.jupiter.store.model.Category;
import com.jupiter.store.model.Product;
import com.jupiter.store.dto.product.CreateProductDTO;
import com.jupiter.store.dto.product.GetProductDTO;
import com.jupiter.store.dto.product.UpdateProductDTO;
import com.jupiter.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductResource {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void addProduct(@RequestBody CreateProductDTO createProductDTO) {
        productService.addProduct(createProductDTO);
    }
    @GetMapping("/search")
    public List<Product> search() {
        return productService.search();
    }
    @GetMapping("/search-detail/{productId}")
    public ResponseEntity<GetProductDTO> searchById(@PathVariable Long productId) {
        return productService.searchById(productId);
    }
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            productService.updateProduct(productId, updateProductDTO);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/add-category-for-product")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void addCategory(@RequestParam Long productId, @RequestBody List<Category> categories) {
        productService.addCategory(productId, categories);
    }
    @DeleteMapping("/delete-category-for-product")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void deleteCategory(@RequestParam Long productId, @RequestBody List<Category> categories) {
        productService.deleteCategory(productId, categories);
    }
}
