package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.dto.ProductReadDTO;
import com.jupiter.store.module.product.dto.ProductVariantAttrValueSimpleReadDTO;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.AttributeRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Page<ProductVariantReadDTO> search(Pageable pageable) {
        Page<ProductVariant> productVariants = productVariantRepository.findAll(pageable);
        return productVariants.map(this::setDetails);
    }

    public ProductVariantReadDTO searchById(Integer variantId) {
        Optional<ProductVariant> productVariant = productVariantRepository.findById(variantId);
        if (productVariant.isPresent()) {
            ProductVariant variant = productVariant.get();
            return setDetails(variant);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy biến thể sản phẩm với ID: " + variantId);
        }
    }

    public List<Integer> getAllIds() {
        return productVariantRepository.findAllIds();
    }

    private ProductVariantReadDTO setDetails(ProductVariant variant) {
        Product product = productRepository.findById(variant.getProductId()).orElse(null);
        List<Category> categories = product != null ? categoryRepository.findByProductId(product.getId()) : List.of();
        ProductReadDTO productDTO = product != null ? new ProductReadDTO(product, categories) : null;

        ProductVariantReadDTO dto = new ProductVariantReadDTO(variant);
        dto.setId(variant.getId());
        dto.setProduct(productDTO);

        // Fetch up to 3 attribute values for this variant
        List<ProductVariantAttrValueSimpleReadDTO> attrValues = attributeService.findByVariantId(variant.getId(), 3);
        dto.setAttrValues(attrValues);

        return dto;
    }
}
