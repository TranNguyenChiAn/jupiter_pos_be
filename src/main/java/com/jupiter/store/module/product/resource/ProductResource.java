package com.jupiter.store.module.product.resource;

import com.jupiter.store.common.dto.PageResponse;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.service.ProductService;
import com.jupiter.store.module.product.service.ProductVariantSearchService;
import com.jupiter.store.module.role.constant.RoleBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ProductVariantSearchService productVariantSearchService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void addProduct(@RequestBody CreateProductDTO createProductDTO) {
        productService.addProduct(createProductDTO);
    }

    @GetMapping("/search")
    public List<Product> search() {
        return productService.search();
    }

    @GetMapping("/search-variant")
    public ResponseEntity<PageResponse<ProductVariantReadDTO>> searchVariant(Pageable pageable) {
        Page<ProductVariantReadDTO> result = productVariantSearchService.searchProductWithVariants(pageable);
        return ResponseEntity.ok(new PageResponse<>(result));
    }

    @GetMapping("/search-detail/{productId}")
    public ResponseEntity<ProductReadDTO> searchById(@PathVariable Integer productId) {
        return productService.searchById(productId);
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public ResponseEntity<String> updateProduct(@PathVariable Integer productId, @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            productService.updateProduct(productId, updateProductDTO);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-category-for-product")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void addCategory(@RequestBody ProductCategoryDTO productCategoryDTO) {
        productService.addCategory(productCategoryDTO);
    }

    @DeleteMapping("/delete-category-for-product")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void deleteCategory(@RequestBody ProductCategoryDTO deleteCategoryDTO) {
        productService.deleteCategory(deleteCategoryDTO);
    }
}
