package com.jupiter.store.service;

import com.jupiter.store.dto.product.ProductVariantAttrValueDto;
import com.jupiter.store.model.*;
import com.jupiter.store.dto.product.*;
import com.jupiter.store.repository.*;
import com.jupiter.store.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductRepository  productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;

    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    public void addProduct(CreateProductDTO createProductDTO) {
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setCreatedBy(currentUserId());
        product = productRepository.save(product);
        saveProductImages(createProductDTO.getImagePath(), product.getId());
        saveProductCategories(createProductDTO.getCategoryId(), product.getId());
        saveProductVariants(createProductDTO.getVariants(), product.getId());
    }
    private void saveProductImages(List<String> imagePaths, Long productId) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String imagePath : imagePaths) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(productId);
                productImage.setImagePath(imagePath);
                productImage.setCreatedBy(currentUserId());
                productImageRepository.save(productImage);
            }
        }
    }
    private void saveProductCategories(List<Long> categoryIds, Long productId) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(productId);
                productCategory.setCategoryId(categoryId);
                productCategory.setCreatedBy(currentUserId());
                productCategoryRepository.save(productCategory);
            }
        }
    }
    private void saveProductVariants(List<ProductVariantDTO> variants, Long productId) {
        if (variants != null && !variants.isEmpty()) {
            for (ProductVariantDTO variantDTO : variants) {
                ProductVariant variant = new ProductVariant();
                variant.setProductId(productId);
                variant.setPrice(variantDTO.getPrice());
                variant.setQuantity(variantDTO.getQuantity());
                variant.setImagePath(variantDTO.getImagePath());
                variant.setCreatedBy(currentUserId());
                productVariantRepository.save(variant);

                for (ProductVariantAttrValueDto attrValue : variantDTO.getAttrAndValues()) {
                    ProductVariantAttrValue productVariantAttrValue = new ProductVariantAttrValue();
                    productVariantAttrValue.setProductId(productId);
                    productVariantAttrValue.setProductVariantId(variant.getId());
                    productVariantAttrValue.setAttrId(attrValue.getAttrId());
                    productVariantAttrValue.setAttrValue(attrValue.getAttrValue());
                    productVariantAttrValue.setCreatedBy(currentUserId());
                    productVariantAttrValueRepository.save(productVariantAttrValue);
                }
            }
        }
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductDTO updateProductDTO) {
        // Validate product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found with ID: " + productId));

        // Update product basic information if provided
        if (updateProductDTO.getName() != null) {
            product.setName(updateProductDTO.getName());
        }
        if (updateProductDTO.getDescription() != null) {
            product.setDescription(updateProductDTO.getDescription());
        }
        
        // Set the last modified by field
        product.setLastModifiedBy(currentUserId());
        
        // Save the updated product
        productRepository.save(product);

        // Update product images if provided
        updateProductImages(productId, updateProductDTO.getImagePaths());
    }

    /**
     * Updates product images efficiently by comparing existing and new images
     * and performing only necessary database operations
     */
    private void updateProductImages(Long productId, List<String> newImagePaths) {
        if (newImagePaths == null || newImagePaths.isEmpty()) {
            return;
        }

        // Get existing images
        List<ProductImage> existingImages = productImageRepository.findByProductId(productId);
        Set<String> newImagePathsSet = new HashSet<>(newImagePaths);
        Set<String> existingImagePaths = new HashSet<>();
        
        // Track existing image paths and identify images to delete
        for (ProductImage existingImage : existingImages) {
            String imagePath = existingImage.getImagePath();
            existingImagePaths.add(imagePath);
            
            if (!newImagePathsSet.contains(imagePath)) {
                productImageRepository.delete(existingImage);
            }
        }
        
        // Add only new images
        for (String imagePath : newImagePaths) {
            if (!existingImagePaths.contains(imagePath)) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(productId);
                productImage.setImagePath(imagePath);
                productImage.setCreatedBy(currentUserId());
                productImageRepository.save(productImage);
            }
        }
    }

    public List<Product> search() {
        return productRepository.findAll();
    }

    public ResponseEntity<GetProductDTO> searchById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found"));
        List<Category> productCategory = categoryRepository.findByProductId(productId);
        List<ProductVariant> productVariants = productVariantRepository.findByProductId(productId);
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        return ResponseEntity.ok().body(new GetProductDTO(product, productCategory, productVariants, productImages));
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found"));
        if (product != null) {
            productVariantRepository.deleteByProductId(productId);
            productImageRepository.deleteByProductId(productId);
            productCategoryRepository.deleteByProductId(productId);
            productRepository.deleteById(productId);
        }else{
            throw new OpenApiResourceNotFoundException("Product not found");
        }
    }

    public void addCategory(Long productId, List<Category> categories) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found"));
        if (product != null) {
            for (Category category : categories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(productId);
                productCategory.setCategoryId(category.getId());
                productCategory.setCreatedBy(currentUserId());
                productCategoryRepository.save(productCategory);
            }
        }else{
            throw new OpenApiResourceNotFoundException("Product not found");
        }
    }

    public void deleteCategory(Long productId, List<Category> categories) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Product not found"));
        if (product != null) {
            for (Category category : categories) {
                productCategoryRepository.deleteByProductIdAndCategoryId(productId, category.getId());
            }
        }
    }
}