package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.constant.ProductStatus;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    @Autowired
    private HelperUtils helperUtils;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Page<ProductVariantReadDTO> search(Pageable pageable, String search, String sort, ProductStatus status) {
        Page<ProductVariant> productVariants;
        search = helperUtils.normalizeSearch(search);
        String productStatus = status != null ? status.toString() : null;
        productVariants = productVariantRepository.search(search, productStatus, pageable);

        List<Integer> invalidIds = new ArrayList<>();
        List<CompletableFuture<ProductVariantReadDTO>> futureList = productVariants.getContent().stream()
                .map(variant -> {
                    CompletableFuture<ProductReadDTO> productFuture = CompletableFuture.supplyAsync(() -> {
                        Product product = null;
                        if (status != null && status.equals(ProductStatus.DANG_BAN)) {
                            product = productRepository.findByIdAndStatus(variant.getProductId(), productStatus);
                        } else {
                            product = productRepository.findById(variant.getProductId()).orElse(null);
                        }
                        if (product == null) {
                            invalidIds.add(variant.getId());
                            return null;
                        }
                        List<Category> categories = categoryRepository.findByProductId(product.getId());
                        return new ProductReadDTO(product, categories);
                    });
                    CompletableFuture<List<ProductVariantAttrValueSimpleReadDTO>> attrFuture = CompletableFuture.supplyAsync(() ->
                            attributeService.findByVariantId(variant.getId(), 3)
                    );
                    CompletableFuture<List<String>> imageFuture = CompletableFuture.supplyAsync(() ->
                            productImageService.findByProductVariantId(variant.getId())
                    );

                    return CompletableFuture.allOf(productFuture, attrFuture, imageFuture)
                            .thenApply(v -> {
                                if (productFuture.join() == null) {
                                    return null;
                                }
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, productVariants.getTotalElements() - invalidIds.size());
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

    public ProductVariantReadDTO setDetailsForVariant(ProductVariant variant) {
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

        CompletableFuture<List<ProductVariantAttrValueSimpleReadDTO>> attrValues = CompletableFuture.supplyAsync(() -> {
            return attributeService.findByVariantId(variant.getId(), null);
        });
        dto.setAttrValues(attrValues.join());

        CompletableFuture<List<String>> imagePaths = CompletableFuture.supplyAsync(() -> {
            return productImageService.findByProductVariantId(variant.getId());
        });
        dto.setImagePaths(imagePaths.join());
        return dto;
    }

    public List<ProductVariantReadDTO> searchVariantsByProducts(List<Integer> productIds) {
        return productVariantRepository.findByProductIdIn(productIds).parallelStream()
                .map(this::setDetailsForVariant)
                .collect(Collectors.toList());
    }
}
