package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.dto.ProductVariantDTO;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.service.ProductVariantService;
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
    public List<ProductVariant> searchProductVariant(@PathVariable Integer productId) {
        return productVariantService.searchProductVariant(productId);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<ProductVariant> addProductVariant(@PathVariable Integer productId,
                                                            @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.addProductVariant(productId, productVariant);
    }

    @PutMapping("/update/{variantId}")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(@PathVariable Integer variantId,
                                                                  @RequestBody ProductVariantDTO productVariant) {
        return productVariantService.updateProductVariant(variantId, productVariant);
    }

    @DeleteMapping("/delete/{variantId}")
    public void deleteProductVariant(@PathVariable Integer variantId) {
        productVariantService.deleteProductVariant(variantId);
    }
}