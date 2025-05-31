package com.jupiter.store.module.product.resource;

import com.jupiter.store.common.dto.PageResponse;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.service.ProductService;
import com.jupiter.store.module.role.constant.RoleBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductResource {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateFullProductDTO createFullProductDTO) {
        log.debug("\t Creating product with details: {}", createFullProductDTO);
        try {
            productService.createFullProduct(createFullProductDTO);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<Product> search() {
        return productService.search();
    }

    @GetMapping("/search-with-variants")
    public ResponseEntity<PageResponse<ProductWithVariantsReadDTO>> searchProductsWithVariants(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort
    ) {
        Page<ProductWithVariantsReadDTO> result = productService.searchProductsWithVariants(pageable, search, sort);
        return ResponseEntity.ok(new PageResponse<>(result));
    }

    @GetMapping("/search-detail/{productId}")
    public ResponseEntity<ProductReadDTO> searchById(@PathVariable Integer productId) {
        return productService.searchById(productId);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer productId, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            productService.updateProduct(productId, updateProductDTO);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-status/{productId}")
    public ResponseEntity<String> updateProductStatus(@PathVariable Integer productId, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            productService.updateProductStatus(productId, updateProductDTO.getStatus());
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-category-for-product")
    public void addCategory(@RequestBody ProductCategoryDTO productCategoryDTO) {
        productService.addCategory(productCategoryDTO);
    }

    @DeleteMapping("/delete-category-for-product")
    public void deleteCategory(@RequestBody ProductCategoryDTO deleteCategoryDTO) {
        productService.deleteCategory(deleteCategoryDTO);
    }
}
