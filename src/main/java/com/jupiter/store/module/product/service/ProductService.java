package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.category.model.Category;
import com.jupiter.store.module.category.repository.CategoryRepository;
import com.jupiter.store.module.product.dto.CreateProductDTO;
import com.jupiter.store.module.product.dto.GetProductDTO;
import com.jupiter.store.module.product.dto.UpdateProductDTO;
import com.jupiter.store.module.product.model.Product;
import com.jupiter.store.module.product.model.ProductCategory;
import com.jupiter.store.module.product.model.ProductImage;
import com.jupiter.store.module.product.model.ProductVariant;
import com.jupiter.store.module.product.repository.*;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantAttrValueRepository productVariantAttrValueRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository, ProductCategoryRepository productCategoryRepository, CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository, ProductVariantAttrValueRepository productVariantAttrValueRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productVariantAttrValueRepository = productVariantAttrValueRepository;
    }

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public void addProduct(CreateProductDTO createProductDTO) {
        Product product = new Product();
        product.setProductName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setStatus(createProductDTO.getStatus());
        product.setCreatedBy(currentUserId());
        product = productRepository.save(product);
        saveProductCategories(createProductDTO.getCategoryId(), product.getId());
    }

    private void saveProductCategories(List<Integer> categoryIds, Integer productId) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Integer categoryId : categoryIds) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(productId);
                productCategory.setCategoryId(categoryId);
                productCategoryRepository.save(productCategory);
            }
        }
    }

    @Transactional
    public void updateProduct(Integer productId, UpdateProductDTO updateProductDTO) {
        // Validate product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found with ID: " + productId));

        product.setProductName(updateProductDTO.getName() != null ? updateProductDTO.getName() : product.getProductName());
        product.setDescription(updateProductDTO.getDescription() != null ? updateProductDTO.getDescription() : product.getDescription());
        product.setLastModifiedBy(currentUserId());
        productRepository.save(product);
    }


    public List<Product> search() {
        return productRepository.findAll();
    }

    public ResponseEntity<GetProductDTO> searchById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm"));
        List<Category> productCategory = categoryRepository.findByProductId(productId);
        return ResponseEntity.ok().body(new GetProductDTO(product, productCategory));
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm"));
        if (product != null) {
            productVariantRepository.deleteByProductId(productId);
            productCategoryRepository.deleteByProductId(productId);
            productRepository.deleteById(productId);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm");
        }
    }

    public void addCategory(Integer productId, List<Category> categories) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm"));
        if (product != null) {
            for (Category category : categories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(productId);
                productCategory.setCategoryId(category.getId());
                productCategoryRepository.save(productCategory);
            }
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm");
        }
    }

    public void deleteCategory(Integer productId, List<Category> categories) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm"));
        if (product != null) {
            for (Category category : categories) {
                productCategoryRepository.deleteByProductIdAndCategoryId(productId, category.getId());
            }
        }
    }
}