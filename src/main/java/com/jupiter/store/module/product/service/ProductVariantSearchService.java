package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.constant.SearchParam;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.jupiter.store.module.product.constant.SearchParam.PRODUCT_NAME;

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

    public Page<ProductVariantReadDTO> search(Pageable pageable, String search, String sort) {
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String property = sortParams[0].trim();
                Sort.Direction direction = Sort.Direction.fromString(sortParams[1].trim());
                pageable = PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(new Sort.Order(direction, property)));
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(sort));
            }
        }
        Page<ProductVariant> productVariants;
        if (search != null && !search.isEmpty()) {
            productVariants = switch (search) {
                case PRODUCT_NAME -> productVariantRepository.findByProductNameContainingIgnoreCase(search, pageable);
//                case "active" -> productVariantRepository.findByStatus("active", pageable);
//                case "inactive" -> productVariantRepository.findByStatus("inactive", pageable);
                default -> productVariantRepository.findAll(pageable);
            };
        } else {
            productVariants = productVariantRepository.findAll(pageable);
        }
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
        // Copy audit fields from entity
        dto.setCreatedBy(variant.getCreatedBy());
        dto.setCreatedDate(variant.getCreatedDate());
        dto.setLastModifiedBy(variant.getLastModifiedBy());
        dto.setLastModifiedDate(variant.getLastModifiedDate());

        // Fetch up to 3 attribute values for this variant
        List<ProductVariantAttrValueSimpleReadDTO> attrValues = attributeService.findByVariantId(variant.getId(), 3);
        dto.setAttrValues(attrValues);

        return dto;
    }
}
