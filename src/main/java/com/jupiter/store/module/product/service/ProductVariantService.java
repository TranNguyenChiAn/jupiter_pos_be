package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.dto.ProductVariantAttrValueDto;
import com.jupiter.store.module.product.dto.ProductVariantDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductAttributeValue;
import com.jupiter.store.module.product.model.ProductVariant;
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

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public ResponseEntity<ProductVariant> addProductVariant(Integer productId, ProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product != null) {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(productId);
            variant.setPrice(productVariant.getPrice());
            variant.setQuantity(productVariant.getQuantity());
            variant.setCreatedBy(currentUserId());
            productVariantRepository.save(variant);

            for (ProductVariantAttrValueDto attrValue : productVariant.getAttrAndValues()) {
                ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                productAttributeValue.setProductVariantId(productId);
                productAttributeValue.setProductVariantId(variant.getId());
                productAttributeValue.setAttrId(attrValue.getAttrId());
                productAttributeValue.setAttrValue(attrValue.getAttrValue());
                productVariantAttrValueRepository.save(productAttributeValue);
            }
            return ResponseEntity.ok(variant);

        } else {
            throw new RuntimeException("Product not found");
        }
    }

    private void saveProductVariants(List<ProductVariantDTO> variants, Integer productId) {
        if (variants != null && !variants.isEmpty()) {
            for (ProductVariantDTO variantDTO : variants) {
                ProductVariant variant = new ProductVariant();
                variant.setProductId(productId);
                variant.setPrice(variantDTO.getPrice());
                variant.setQuantity(variantDTO.getQuantity());
                variant.setCreatedBy(currentUserId());
                productVariantRepository.save(variant);

                for (ProductVariantAttrValueDto attrValue : variantDTO.getAttrAndValues()) {
                    ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                    productAttributeValue.setProductVariantId(variant.getId());
                    productAttributeValue.setAttrId(attrValue.getAttrId());
                    productAttributeValue.setAttrValue(attrValue.getAttrValue());
                    productVariantAttrValueRepository.save(productAttributeValue);
                }
            }
        }
    }

    public ResponseEntity<ProductVariantDTO> updateProductVariant(Integer variantId, ProductVariantDTO productVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        variant.setPrice(productVariant.getPrice() != 0 ? productVariant.getPrice() : variant.getPrice());
        variant.setQuantity(productVariant.getQuantity() != 0 ? productVariant.getQuantity() : variant.getQuantity());
        //TODO: update product image
        variant.setLastModifiedBy(currentUserId());
        productVariantRepository.save(variant);

        for (ProductVariantAttrValueDto attrValue : productVariant.getAttrAndValues()) {
            ProductAttributeValue productAttributeValue = productVariantAttrValueRepository.findByProductIdAndAttrId(variantId, attrValue.getAttrId()).orElseThrow(() -> new RuntimeException("Product variant attribute value not found"));
            productAttributeValue.setAttrValue(attrValue.getAttrValue());
            productVariantAttrValueRepository.save(productAttributeValue);
        }
        return ResponseEntity.ok(productVariant);
    }

    public void deleteProductVariant(Integer variantId) {
        productVariantRepository.deleteById(variantId);
    }

    public List<ProductVariant> searchProductVariant(Integer productId) {
        return productVariantRepository.findByProductId(productId);
    }
}
