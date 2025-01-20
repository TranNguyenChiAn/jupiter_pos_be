package com.jupiter.store.service;

import com.jupiter.store.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    public void deleteProductVariant(Long variantId) {
        productVariantRepository.deleteById(variantId);
    }
}
