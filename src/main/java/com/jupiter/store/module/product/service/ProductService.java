package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductCategory;
import com.jupiter.store.module.product.repository.ProductCategoryRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AttributeService attributeService;
    private final ProductVariantService productVariantService;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
                          CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository,
                          AttributeService attributeService, ProductVariantService productVariantService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.attributeService = attributeService;
        this.productVariantService = productVariantService;
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
    public void updateProduct(Integer productId, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found with ID: " + productId));

        product.setProductName(updateProductDTO.getProductName() != null ? updateProductDTO.getProductName() : product.getProductName());
        product.setDescription(updateProductDTO.getDescription() != null ? updateProductDTO.getDescription() : product.getDescription());
        product.setStatus(updateProductDTO.getStatus() != null ? updateProductDTO.getStatus() : product.getStatus());
        product.setLastModifiedBy(currentUserId());
        productRepository.save(product);
    }

    public List<Product> search() {
        return productRepository.findAll();
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