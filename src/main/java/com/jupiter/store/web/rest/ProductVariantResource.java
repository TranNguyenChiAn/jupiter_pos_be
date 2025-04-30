package com.jupiter.store.web.rest;

import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.dto.product.ProductVariantDTO;
import com.jupiter.store.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantResource {
    @Autowired
    private ProductVariantService productVariantService;
    @GetMapping("/search/{productId}")
    public List<ProductVariant> searchProductVariant(@PathVariable Long productId) {
        return productVariantService.searchProductVariant(productId);
    }
    @PostMapping("/add/{productId}")
    public ResponseEntity<ProductVariant> addProductVariant(@PathVariable Long productId,
                                                            @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.addProductVariant(productId, productVariant);
    }
    @PutMapping("/update/{variantId}")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(@PathVariable Long variantId,
                                                                  @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.updateProductVariant(variantId, productVariant);
    }
    @DeleteMapping("/delete/{variantId}")
    public void deleteProductVariant(@PathVariable Long variantId) {
        productVariantService.deleteProductVariant(variantId);
    }
}