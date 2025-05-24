package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.dto.ProductReadDTO;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.dto.ProductWithVariantsReadDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.AttributeRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantSearchService {
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private AttributeService attributeService;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Page<ProductVariantReadDTO> searchProductWithVariants(Pageable pageable) {
        Page<ProductVariant> productVariants = productVariantRepository.findAll(pageable);
        return productVariants.map(variant -> {
            Product product = productRepository.findById(variant.getProductId()).orElse(null);
            List<Category> categories = product != null ? categoryRepository.findByProductId(product.getId()) : List.of();
            ProductReadDTO productDTO = product != null ? new ProductReadDTO(product, categories) : null;

            ProductVariantReadDTO dto = new ProductVariantReadDTO(variant);
            dto.setId(variant.getId());
            dto.setProduct(productDTO);

            // Fetch up to 3 attribute values for this variant
            List<String> attrValues = attributeService.findByVariantId(variant.getId(), 3)
                    .stream()
                    .map(attrVal -> attrVal.getAttrName() + ": " + attrVal.getAttrValue())
                    .toList();
            dto.setAttrValues(attrValues);

            return dto;
        });
    }
}
