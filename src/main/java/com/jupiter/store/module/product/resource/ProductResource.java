package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.product.dto.CreateProductDTO;
import com.jupiter.store.module.product.dto.GetProductDTO;
import com.jupiter.store.module.product.dto.UpdateProductDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.service.ProductService;
import com.jupiter.store.module.role.constant.RoleBase;
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
