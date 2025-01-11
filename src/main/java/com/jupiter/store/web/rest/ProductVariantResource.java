package com.jupiter.store.web.rest;

import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-variants")
public class ProductVariantResource {
    @Autowired
    private ProductVariantService productVariantService;
}
