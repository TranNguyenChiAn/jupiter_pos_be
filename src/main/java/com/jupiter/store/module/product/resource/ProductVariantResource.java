package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.dto.CreateProductVariantDTO;
import com.jupiter.store.module.product.dto.GetAllProductVariantDTO;
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
    public List<GetAllProductVariantDTO> searchProductVariant(@PathVariable Integer productId) {
        return productVariantService.searchProductVariant(productId);
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<CreateProductVariantDTO> addProductVariant(@PathVariable Integer productId,
                                                            @RequestBody CreateProductVariantDTO productVariant) {
        return productVariantService.addProductVariant(productId, productVariant);
    }

    @PutMapping("/update/{variantId}")
    public ResponseEntity<CreateProductVariantDTO> updateProductVariant(@PathVariable Integer variantId,
                                                                        @RequestBody CreateProductVariantDTO productVariant) {
        return productVariantService.updateProductVariant(variantId, productVariant);
    }

    @DeleteMapping("/delete/{variantId}")
    public void deleteProductVariant(@PathVariable Integer variantId) {
        productVariantService.deleteProductVariant(variantId);
    }
}