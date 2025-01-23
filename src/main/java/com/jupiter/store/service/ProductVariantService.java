package com.jupiter.store.service;

import com.jupiter.store.domain.Product;
import com.jupiter.store.domain.ProductVariant;
import com.jupiter.store.dto.product.ProductVariantDTO;
import com.jupiter.store.dto.product.UpdateProductVariantDTO;
import com.jupiter.store.repository.ProductRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<ProductVariant> addProductVariant(Long productId, ProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product != null) {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(productId);
            variant.setPrice(productVariant.getPrice());
            variant.setQuantity(productVariant.getQuantity());
            variant.setColor(productVariant.getColor());
            variant.setSizeId(productVariant.getSizeId());
            variant.setImagePath(productVariant.getImagePath());
            variant.setCreatedBy(3481888888888888L);
            return ResponseEntity.ok(productVariantRepository.save(variant));
        }else {
            throw new RuntimeException("Product not found");
        }
    }

    public ResponseEntity<ProductVariantDTO> updateProductVariant(Long variantId, ProductVariantDTO productVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        variant.setPrice(productVariant.getPrice() != 0 ? productVariant.getPrice() : variant.getPrice());
        variant.setQuantity(productVariant.getQuantity() != 0 ? productVariant.getQuantity() : variant.getQuantity());
        variant.setColor(productVariant.getColor() != null ? productVariant.getColor() : variant.getColor());
        variant.setSizeId(productVariant.getSizeId() != null ? productVariant.getSizeId() : variant.getSizeId());
        variant.setImagePath(productVariant.getImagePath() != null ? productVariant.getImagePath() : variant.getImagePath());
        productVariantRepository.save(variant);
        return ResponseEntity.ok(new ProductVariantDTO(variant));
    }
    public void deleteProductVariant(Long variantId) {
        productVariantRepository.deleteById(variantId);
    }

    public List<ProductVariant> searchProductVariant(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }
}
