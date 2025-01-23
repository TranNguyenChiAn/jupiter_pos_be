package com.jupiter.store.web.rest;

import com.jupiter.store.domain.Category;
import com.jupiter.store.domain.Product;
import com.jupiter.store.dto.product.CreateProductDTO;
import com.jupiter.store.dto.product.GetProductDTO;
import com.jupiter.store.dto.product.ProductDTO;
import com.jupiter.store.dto.product.UpdateProductDTO;
import com.jupiter.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public void addProduct(@RequestBody CreateProductDTO createProductDTO) {
        productService.addProduct(createProductDTO);
    }

    @GetMapping("/search")
    public List<Product> search() {
        return productService.search();
    }

    @GetMapping("/search/{productId}")
    public ResponseEntity<GetProductDTO> searchById(@RequestParam Long productId) {
        return productService.searchById(productId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@RequestParam Long productId, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            productService.updateProduct(productId, updateProductDTO);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-category-for-product")
    public void addCategory(@RequestParam Long productId, @RequestBody List<Category> categories) {
        productService.addCategory(productId, categories);

    }

    @DeleteMapping("/delete-category-for-product")
    public void deleteCategory(@RequestParam Long productId, @RequestBody List<Category> categories) {
        productService.deleteCategory(productId, categories);
    }
}
