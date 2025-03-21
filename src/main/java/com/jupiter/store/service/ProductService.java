package com.jupiter.store.service;

import com.jupiter.store.model.*;
import com.jupiter.store.dto.product.*;
import com.jupiter.store.repository.*;
import jakarta.transaction.Transactional;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public void addProduct(CreateProductDTO createProductDTO) {
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setCreatedBy(3481888888888888L);
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
                productImage.setCreatedBy(3481888888888888L);
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
                productCategory.setCreatedBy(3481888888888888L);
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
                variant.setAttributeId(variantDTO.getAttributeId());
                variant.setAttributeValue(variantDTO.getAttributeValue());
                variant.setImagePath(variantDTO.getImagePath());
                variant.setCreatedBy(3481888888888888L);
                productVariantRepository.save(variant);
            }
        }
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductDTO updateProductDTO) {
        Optional<Product> existingProductOptional = productRepository.findById(productId);
        if (!existingProductOptional.isPresent()) {
            throw new OpenApiResourceNotFoundException("Product not found with ID: " + productId);
        }

        Product product = existingProductOptional.get();
        product.setName(updateProductDTO.getName() != null ? updateProductDTO.getName() : product.getName());
        product.setDescription(updateProductDTO.getDescription() != null ? updateProductDTO.getDescription() : product.getDescription());
        productRepository.save(product);

        List<String> productImages = updateProductDTO.getImagePaths();
        if (productImages != null && !productImages.isEmpty()) {
            List<ProductImage> existingImages = productImageRepository.findByProductId(productId);
            Set<String> newImagePaths = new HashSet<>(productImages);

            // Xóa những ảnh cũ không có trong request (bị xóa)
            for (ProductImage existingImage : existingImages) {
                if (!newImagePaths.contains(existingImage.getImagePath())) {
                    productImageRepository.delete(existingImage);
                }
            }

            // Thêm ảnh mới hoặc cập nhật
            for (String image : productImages) {
                Optional<ProductImage> existingImage = existingImages.stream()
                        .filter(img -> img.getImagePath().equals(image))
                        .findFirst();

                if (existingImage.isEmpty()) {  // Nếu ảnh mới không có trong DB, thêm mới
                    ProductImage productImage = new ProductImage();
                    productImage.setProductId(product.getId());
                    productImage.setImagePath(image);
                    productImage.setCreatedBy(3481888888888888L);
                    productImageRepository.save(productImage);
                }
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
                productCategory.setCreatedBy(3481888888888888L);
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