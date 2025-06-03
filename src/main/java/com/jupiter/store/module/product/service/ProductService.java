package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.dto.CategoryDTO;
import com.jupiter.store.module.category.dto.IProductCategoryQueryDTO;
import com.jupiter.store.module.category.dto.ProductCategoryQueryDTO;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.constant.ProductStatus;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductCategory;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.ProductCategoryRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AttributeService attributeService;
    private final ProductVariantService productVariantService;
    private final ProductVariantSearchService productVariantSearchService;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
                          CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository,
                          AttributeService attributeService, ProductVariantService productVariantService, ProductVariantSearchService productVariantSearchService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.attributeService = attributeService;
        this.productVariantService = productVariantService;
        this.productVariantSearchService = productVariantSearchService;
    }

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

//    public void addProduct(CreateProductDTO createProductDTO) {
//        Product product = new Product();
//        product.setProductName(createProductDTO.getName());
//        product.setDescription(createProductDTO.getDescription());
//        product.setStatus(createProductDTO.getStatus());
//        product.setCreatedBy(currentUserId());
//        product = productRepository.save(product);
//        saveProductCategories(createProductDTO.getCategoryId(), product.getId());
//    }

    @Transactional
    public void createFullProduct(CreateFullProductDTO dto) {
        // Create product
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus());
        product = setAuditFields(product, true);
        Product savedProduct = productRepository.save(product);

        // Save related categories
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            List<ProductCategory> productCategories = categories.stream()
                    .map(category -> new ProductCategory(savedProduct, category))
                    .toList();
            productCategoryRepository.saveAll(productCategories);
        }

        // Create each product variant along with its attribute values
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            for (CreateProductVariantDTO variantDTO : dto.getVariants()) {
                productVariantService.addProductVariant(savedProduct.getId(), variantDTO);
            }
        }
    }

//    private void saveProductCategories(List<Integer> categoryIds, Integer productId) {
//        if (categoryIds != null && !categoryIds.isEmpty()) {
//            for (Integer categoryId : categoryIds) {
//                ProductCategory productCategory = new ProductCategory();
//                productCategory.setProductId(productId);
//                productCategory.setCategoryId(categoryId);
//                productCategoryRepository.save(productCategory);
//            }
//        }
//    }

    @Transactional
    public void updateProduct(Integer productId, UpdateProductDTO dto) {
        // Assume that dto contains productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        // Update product basic details and audit fields (isCreate is false for update)
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus());
        product = setAuditFields(product, false);
        Product savedProduct = productRepository.save(product);

        // Update categories: remove existing entries and save the new ones
        productCategoryRepository.deleteByProductId(savedProduct.getId());
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            List<ProductCategory> productCategories = categories.stream()
                    .map(category -> new ProductCategory(savedProduct, category))
                    .toList();
            productCategoryRepository.saveAll(productCategories);
        }
    }

    @Transactional
    public void updateProductStatus(Integer productId, ProductStatus productStatus) {
        // Assume that dto contains productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

        // Update product basic details and audit fields (isCreate is false for update)
        product.setStatus(productStatus);
        product = setAuditFields(product, false);
        productRepository.save(product);
    }

    public List<Product> search() {
        return productRepository.findAll();
    }

    public Page<ProductWithVariantsReadDTO> searchProductsWithVariants(
            String search,
            SearchProductsFilterDTO filter,
            String sort,
            Pageable pageable
    ) {
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length > 2) {
                sortParams = new String[]{"last_modified_date", "desc"};
            }
            if (!((search == null || search.trim().isBlank()) && (filter == null))) {
                String column = sortParams[0].trim();
                column = column.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
                sortParams[0] = column;
            }
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
        if (search == null || search.trim().isBlank()) {
            search = "";
        }
        Integer categoryId = null;
        String status = null;
        if (filter != null) {
            if (filter.getCategoryId() != null) {
                categoryId = filter.getCategoryId();
            }
            if (filter.getStatus() != null) {
                if (Stream.of(ProductStatus.ACTIVE, ProductStatus.INACTIVE, ProductStatus.DELETED).anyMatch(s -> s.toString().equalsIgnoreCase(filter.getStatus()))) {
                    status = filter.getStatus();
                }
            }
        }
        Page<Product> products = productRepository.searchProduct(search, categoryId, status, pageable);
        List<Product> productList = products.getContent();
        if (productList.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // Get list of product IDs to load associated entities in batch.
        List<Integer> productIds = productList.parallelStream()
                .map(Product::getId)
                .toList();

        // Launch asynchronous batch queries
        CompletableFuture<List<ProductCategoryQueryDTO>> categoriesFuture = CompletableFuture.supplyAsync(() -> {
                    List<ProductCategoryQueryDTO> list = getProductsCategoriesFromObjects(categoryRepository.findByProductIdIn(productIds));
                    return list;
                }
        );
        CompletableFuture<List<ProductVariantReadDTO>> variantsFuture = CompletableFuture.supplyAsync(() ->
                {
                    List<ProductVariantReadDTO> list = productVariantSearchService.searchVariantsByProducts(productIds);
                    return list;
                }
        );

        // Wait for both futures to complete.
        CompletableFuture.allOf(categoriesFuture, variantsFuture).join();

        List<ProductCategoryQueryDTO> allCategories = categoriesFuture.join();
        List<ProductVariantReadDTO> allVariants = variantsFuture.join();

        // Group categories and variants by product id.
        Map<Integer, List<ProductCategoryQueryDTO>> categoriesMap = allCategories.parallelStream()
                .collect(Collectors.groupingBy(ProductCategoryQueryDTO::getProductId));
        Map<Integer, List<ProductVariantReadDTO>> variantsMap = allVariants.parallelStream()
                .collect(Collectors.groupingBy(dto -> dto.getProduct().getProductId()));

        // Build the final DTO list.
        List<ProductWithVariantsReadDTO> result = productList.parallelStream().map(product -> {
            List<Category> productCategories = categoriesMap.getOrDefault(product.getId(), List.of()).parallelStream()
                    .map(ProductCategoryQueryDTO::toCategory).collect(Collectors.toList());
            List<ProductVariantReadDTO> productVariants = variantsMap.getOrDefault(product.getId(), List.of());
            return new ProductWithVariantsReadDTO(new ProductReadDTO(product, productCategories), productVariants);
        }).toList();

        return new PageImpl<>(result, pageable, products.getTotalElements());
    }

    public List<ProductCategoryQueryDTO> getProductsCategoriesFromObjects(List<Object[]> rows) {
        List<ProductCategoryQueryDTO> categories = new ArrayList<>();
        for (Object[] row : rows) {
            ProductCategoryQueryDTO c = new ProductCategoryQueryDTO();
            c.setId((Integer) row[0]);
            c.setCategoryName((String) row[1]);
            c.setProductId((Integer) row[2]);
            categories.add(c);
        }
        return categories;
    }

    public ResponseEntity<ProductReadDTO> searchById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm với id: " + productId));
        List<Category> productCategory = categoryRepository.findByProductId(productId);
        return ResponseEntity.ok().body(new ProductReadDTO(product, productCategory));
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        if (product != null) {
            productVariantRepository.deleteByProductId(productId);
            productCategoryRepository.deleteByProductId(productId);
            productRepository.deleteById(productId);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!");
        }
    }

    public void addCategory(ProductCategoryDTO productCategoryDTO) {
        Product product = productRepository.findById(productCategoryDTO.getProductId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        List<Category> categories = categoryRepository.findAllById(productCategoryDTO.getCategoryIds());
        if (product != null && !categories.isEmpty()) {
            for (Category category : categories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(product);
                productCategory.setCategoryId(category);
                productCategoryRepository.save(productCategory);
            }
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!");
        }
    }

    public void deleteCategory(ProductCategoryDTO deleteCategoryDTO) {
        Product product = productRepository.findById(deleteCategoryDTO.getProductId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm!"));
        if (product != null) {
            for (Integer categoryId : deleteCategoryDTO.getCategoryIds()) {
                productCategoryRepository.deleteByProductIdAndCategoryId(deleteCategoryDTO.getProductId(), categoryId);
            }
        }
    }

    private Product setAuditFields(Product product, Boolean isCreate) {
        if (isCreate) {
            product.setCreatedBy(currentUserId());
            product.setCreatedDate(LocalDateTime.now());
        }
        product.setLastModifiedBy(currentUserId());
        product.setLastModifiedDate(LocalDateTime.now());
        return product;
    }
}