package com.jupiter.store.service;

import com.jupiter.store.domain.Product;
import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.dto.CreateProductDTO;
import com.jupiter.store.dto.ProductVariantDTO;
import com.jupiter.store.repository.ProductRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductRepository productRepository;

}
