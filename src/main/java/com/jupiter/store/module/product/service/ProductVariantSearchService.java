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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductImageService productImageService;

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
                        Sort.by(new Sort.Order(direction, property))
                );
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

        // Process each variant in parallel using CompletableFuture.
        List<CompletableFuture<ProductVariantReadDTO>> futureList = productVariants.getContent().stream()
                .map(variant -> {
                    CompletableFuture<ProductReadDTO> productFuture = CompletableFuture.supplyAsync(() -> {
                        Product product = productRepository.findById(variant.getProductId()).orElse(null);
                        List<Category> categories = product != null ? categoryRepository.findByProductId(product.getId()) : List.of();
                        return product != null ? new ProductReadDTO(product, categories) : null;
                    });
                    CompletableFuture<List<ProductVariantAttrValueSimpleReadDTO>> attrFuture = CompletableFuture.supplyAsync(() ->
                            attributeService.findByVariantId(variant.getId(), 3)
                    );
                    CompletableFuture<List<String>> imageFuture = CompletableFuture.supplyAsync(() ->
                            productImageService.findByProductVariantId(variant.getId())
                    );

                    return CompletableFuture.allOf(productFuture, attrFuture, imageFuture)
                            .thenApply(v -> {
                                ProductReadDTO productDTO = productFuture.join();
                                List<ProductVariantAttrValueSimpleReadDTO> attrValues = attrFuture.join();
                                List<String> imagePaths = imageFuture.join();
                                ProductVariantReadDTO dto = new ProductVariantReadDTO(variant);
                                dto.setId(variant.getId());
                                dto.setProduct(productDTO);
                                dto.setCreatedBy(variant.getCreatedBy());
                                dto.setCreatedDate(variant.getCreatedDate());
                                dto.setLastModifiedBy(variant.getLastModifiedBy());
                                dto.setLastModifiedDate(variant.getLastModifiedDate());
                                dto.setAttrValues(attrValues);
                                dto.setImagePaths(imagePaths);
                                return dto;
                            });
                }).toList();

        List<ProductVariantReadDTO> dtos = futureList.stream()
                .map(CompletableFuture::join)
                .toList();

        return new PageImpl<>(dtos, pageable, productVariants.getTotalElements());
    }

    public ProductVariantReadDTO searchById(Integer variantId) {
        Optional<ProductVariant> productVariant = productVariantRepository.findById(variantId);
        if (productVariant.isPresent()) {
            ProductVariant variant = productVariant.get();
            return setDetailsForVariant(variant);
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
        // Copy audit fields from entity
        dto.setProduct(productDTO);
        dto.setCreatedBy(variant.getCreatedBy());
        dto.setCreatedDate(variant.getCreatedDate());
        dto.setLastModifiedBy(variant.getLastModifiedBy());
        dto.setLastModifiedDate(variant.getLastModifiedDate());

        // Fetch up to 3 attribute values for this variant
        List<ProductVariantAttrValueSimpleReadDTO> attrValues = attributeService.findByVariantId(variant.getId(), 3);
        dto.setAttrValues(attrValues);

        List<String> imagePaths = productImageService.findByProductVariantId(variant.getId());
        dto.setImagePaths(imagePaths);
        return dto;
    }

    private ProductVariantReadDTO setDetailsForVariant(ProductVariant variant) {
        Product product = productRepository.findById(variant.getProductId()).orElse(null);
        List<Category> categories = product != null ? categoryRepository.findByProductId(product.getId()) : List.of();
        ProductReadDTO productDTO = product != null ? new ProductReadDTO(product, categories) : null;

        ProductVariantReadDTO dto = new ProductVariantReadDTO(variant);
        dto.setId(variant.getId());
        // Copy audit fields from entity
        dto.setProduct(productDTO);
        dto.setCreatedBy(variant.getCreatedBy());
        dto.setCreatedDate(variant.getCreatedDate());
        dto.setLastModifiedBy(variant.getLastModifiedBy());
        dto.setLastModifiedDate(variant.getLastModifiedDate());

        List<ProductVariantAttrValueSimpleReadDTO> attrValues = attributeService.findByVariantId(variant.getId(), null);
        dto.setAttrValues(attrValues);

        List<String> imagePaths = productImageService.findByProductVariantId(variant.getId());
        dto.setImagePaths(imagePaths);
        return dto;
    }

    public List<ProductVariantReadDTO> searchVariantsByProducts(List<Integer> productIds) {
        return productVariantRepository.findByProductIdIn(productIds).stream()
                .map(ProductVariant::toProductVariantReadDTO)
                .collect(Collectors.toList());
    }
}
