package com.jupiter.store.web.rest;

import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-variants")
public class ProductVariantResource {
    @Autowired
    private ProductVariantService productVariantService;

//    @DeleteMapping("/delete/{variantId}")
//    public void deleteProductVariant(Long variantId) {
//        productVariantService.deleteProductVariant(variantId);
//    }
}
