package com.jupiter.store.service;

import com.jupiter.store.model.Product;
import com.jupiter.store.model.ProductVariant;
import com.jupiter.store.dto.product.ProductVariantDTO;
import com.jupiter.store.repository.ProductRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import com.jupiter.store.utils.SecurityUtils;
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
    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    public ResponseEntity<ProductVariant> addProductVariant(Long productId, ProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product != null) {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(productId);
            variant.setPrice(productVariant.getPrice());
            variant.setQuantity(productVariant.getQuantity());
            variant.setAttributeId(productVariant.getAttributeId());
            variant.setAttributeValue(productVariant.getAttributeValue());
            variant.setImagePath(productVariant.getImagePath());
            variant.setCreatedBy(currentUserId());
            return ResponseEntity.ok(productVariantRepository.save(variant));
        }else {
            throw new RuntimeException("Product not found");
        }
    }

    public ResponseEntity<ProductVariantDTO> updateProductVariant(Long variantId, ProductVariantDTO productVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        variant.setPrice(productVariant.getPrice() != 0 ? productVariant.getPrice() : variant.getPrice());
        variant.setQuantity(productVariant.getQuantity() != 0 ? productVariant.getQuantity() : variant.getQuantity());
        variant.setAttributeId(productVariant.getAttributeId() != null ? productVariant.getAttributeId(): variant.getAttributeId());
        variant.setAttributeValue(productVariant.getAttributeValue() != null ? productVariant.getAttributeValue() : variant.getAttributeValue());
        variant.setImagePath(productVariant.getImagePath() != null ? productVariant.getImagePath() : variant.getImagePath());
        variant.setLastModifiedBy(currentUserId());
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
