package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.*;
import com.jupiter.store.module.product.repository.ProductImageRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductImageService productImageService;

    public ResponseEntity<CreateProductVariantDTO> addProductVariant(Integer productId, CreateProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
        if (product != null) {
            Integer currentUserId = SecurityUtils.getCurrentUserId();
            ProductVariant variant = new ProductVariant(null, productId, productVariant.getCostPrice(),
                    productVariant.getPrice(), productVariant.getQuantity(),
                    productVariant.getUnitId(), productVariant.getSku(),
                    productVariant.getBarcode(), productVariant.getExpiryDate(),
                    productVariant.getStatus());
            variant = setAuditFields(variant, true);
            productVariantRepository.save(variant);

            productImageService.saveProductImages(variant.getId(), productVariant.getImagePaths());

            ProductVariant finalVariant = variant;
            List<ProductAttributeValue> attributeValues = productVariant.getAttrAndValues().stream()
                    .map(attrValue -> new ProductAttributeValue(finalVariant.getId(), attrValue, currentUserId))
                    .collect(Collectors.toList());
            productVariantAttrValueRepository.saveAll(attributeValues);
            return ResponseEntity.ok(productVariant);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + productId);
        }
    }

    public ResponseEntity<CreateProductVariantDTO> updateProductVariant(Integer variantId, CreateProductVariantDTO newProductVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        Long costPrice = newProductVariant.getCostPrice();
        if (costPrice == null) {
            costPrice = 0L; // Default to 0 if costPrice is null
        }
        variant.setCostPrice(costPrice < 0 ? 0L : costPrice); // Ensure costPrice is not negative
        Long price = newProductVariant.getPrice();
        if (price == null) {
            price = 0L; // Default to 0 if price is null
        }
        variant.setPrice(price < 0 ? 0L : price); // Ensure price is not negative
        Integer quantity = newProductVariant.getQuantity();
        if (quantity == null) {
            quantity = 0; // Default to 0 if quantity is null
        }
        variant.setQuantity(quantity < 0 ? 0 : quantity); // Ensure quantity is not negative
        Integer unitId = newProductVariant.getUnitId();
        variant.setUnitId(unitId);
        variant.setSku(newProductVariant.getSku());
        variant.setBarcode(newProductVariant.getBarcode());
        variant.setExpiryDate(newProductVariant.getExpiryDate());
        variant.setStatus(newProductVariant.getStatus());
        variant = setAuditFields(variant, false);
        productVariantRepository.save(variant);
        if (newProductVariant.getImagePaths() != null && !newProductVariant.getImagePaths().isEmpty()) {
            productImageService.updateProductImages(variantId, newProductVariant.getImagePaths());
        }
//        else {
//            List<ProductImage> productImages = productImageRepository.findByProductVariantId(variantId);
//            variant.setIm
//        }

        if (newProductVariant.getAttrAndValues() != null && !newProductVariant.getAttrAndValues().isEmpty()) {
            // Delete all existing attribute values for the variant
            List<ProductAttributeValue> existingValues = productVariantAttrValueRepository.findByProductVariantId(variantId);
            if (existingValues != null && !existingValues.isEmpty()) {
                productVariantAttrValueRepository.deleteAll(existingValues);
            }
            // Save new attribute values
            List<ProductAttributeValue> newValues = newProductVariant.getAttrAndValues().stream()
                    .map(attrValue -> {
                        ProductAttributeValue value = new ProductAttributeValue();
                        value.setProductVariantId(variantId);
                        value.setAttrId(attrValue.getAttrId());
                        value.setAttrValue(attrValue.getAttrValue());
                        value.setUnitId(attrValue.getUnitId());
                        value.setCreatedBy(SecurityUtils.getCurrentUserId());
                        value.setLastModifiedBy(SecurityUtils.getCurrentUserId());
                        // Set additional fields if needed
                        return value;
                    }).collect(Collectors.toList());
            productVariantAttrValueRepository.saveAll(newValues);
        }
        return ResponseEntity.ok(newProductVariant);
    }

    public void deleteProductVariant(Integer productVariantId) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productVariantId));
        List<ProductAttributeValue> productAttributeValue = productVariantAttrValueRepository.findByProductVariantId(productVariantId);
        List<ProductImage> productImages = productImageRepository.findByProductVariantId(productVariantId);
        if (productAttributeValue != null && !productAttributeValue.isEmpty()) {
            productVariantAttrValueRepository.deleteAll(productAttributeValue);
        }
        if (productImages != null && !productImages.isEmpty()) {
            productImageRepository.deleteAll(productImages);
        }
        productVariantRepository.delete(productVariant);

    }

    private ProductVariant setAuditFields(ProductVariant variant, Boolean isCreate) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (isCreate) {
            variant.setCreatedBy(currentUserId);
            variant.setCreatedDate(LocalDateTime.now());
        }
        variant.setLastModifiedBy(currentUserId);
        variant.setLastModifiedDate(LocalDateTime.now());
        return variant;
    }
}
