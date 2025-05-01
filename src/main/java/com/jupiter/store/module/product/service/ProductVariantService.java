package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.dto.ProductVariantAttrValueDto;
import com.jupiter.store.module.product.dto.ProductVariantDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.model.ProductVariantAttrValue;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;

    @Autowired
    private ProductRepository productRepository;

    public static Long currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public ResponseEntity<ProductVariant> addProductVariant(Long productId, ProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product != null) {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(productId);
            variant.setPrice(productVariant.getPrice());
            variant.setQuantity(productVariant.getQuantity());
            variant.setImagePath(productVariant.getImagePath());
            variant.setCreatedBy(currentUserId());
            productVariantRepository.save(variant);

            for (ProductVariantAttrValueDto attrValue : productVariant.getAttrAndValues()) {
                ProductVariantAttrValue productVariantAttrValue = new ProductVariantAttrValue();
                productVariantAttrValue.setProductId(productId);
                productVariantAttrValue.setProductVariantId(variant.getId());
                productVariantAttrValue.setAttrId(attrValue.getAttrId());
                productVariantAttrValue.setAttrValue(attrValue.getAttrValue());
                productVariantAttrValueRepository.save(productVariantAttrValue);
            }
            return ResponseEntity.ok(variant);

        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public ResponseEntity<ProductVariantDTO> updateProductVariant(Long variantId, ProductVariantDTO productVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        variant.setPrice(productVariant.getPrice() != 0 ? productVariant.getPrice() : variant.getPrice());
        variant.setQuantity(productVariant.getQuantity() != 0 ? productVariant.getQuantity() : variant.getQuantity());
        variant.setImagePath(productVariant.getImagePath() != null ? productVariant.getImagePath() : variant.getImagePath());
        variant.setLastModifiedBy(currentUserId());
        productVariantRepository.save(variant);

        for (ProductVariantAttrValueDto attrValue : productVariant.getAttrAndValues()) {
            ProductVariantAttrValue productVariantAttrValue = productVariantAttrValueRepository.findByProductIdAndAttrId(variantId, attrValue.getAttrId()).orElseThrow(() -> new RuntimeException("Product variant attribute value not found"));
            productVariantAttrValue.setAttrValue(attrValue.getAttrValue());
            productVariantAttrValue.setLastModifiedBy(currentUserId());
            productVariantAttrValueRepository.save(productVariantAttrValue);
        }
        return ResponseEntity.ok(productVariant);
    }

    public void deleteProductVariant(Long variantId) {
        productVariantRepository.deleteById(variantId);
    }

    public List<ProductVariant> searchProductVariant(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }
}
