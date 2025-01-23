package com.jupiter.store.web.rest;

import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.dto.product.ProductVariantDTO;
import com.jupiter.store.dto.product.UpdateProductVariantDTO;
import com.jupiter.store.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-variants")
public class ProductVariantResource {
    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping("/search/{productId}")
    public List<ProductVariant> searchProductVariant(@RequestParam Long productId) {
        return productVariantService.searchProductVariant(productId);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<ProductVariant> addProductVariant(@RequestParam Long productId, @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.addProductVariant(productId, productVariant);
    }

    @PutMapping("/update/{variantId}")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(@RequestParam Long variantId, @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.updateProductVariant(variantId, productVariant);
    }

    @DeleteMapping("/delete/{variantId}")
    public void deleteProductVariant(Long variantId) {
        productVariantService.deleteProductVariant(variantId);
    }
}
