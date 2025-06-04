package com.jupiter.store.module.product.resource;

import com.jupiter.store.common.dto.PageResponse;
import com.jupiter.store.module.product.dto.CreateProductVariantDTO;
import com.jupiter.store.module.product.dto.GetAllProductVariantDTO;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.service.ProductVariantSearchService;
import com.jupiter.store.module.product.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantResource {
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private ProductVariantSearchService productVariantSearchService;

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductVariantReadDTO>> searchVariant(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort) {
        Page<ProductVariantReadDTO> result = productVariantSearchService.search(pageable, search, sort);
        return ResponseEntity.ok(new PageResponse<>(result));
    }

    @GetMapping("/search-variant/{variantId}")
    public ResponseEntity<ProductVariantReadDTO> searchByVariantId(@PathVariable Integer variantId) {
        ProductVariantReadDTO result = productVariantSearchService.searchById(variantId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all-ids")
    public ResponseEntity<List<Integer>> getAllIds() {
        List<Integer> result = productVariantSearchService.getAllIds();
        return ResponseEntity.ok(result);
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