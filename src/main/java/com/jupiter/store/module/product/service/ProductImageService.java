package com.jupiter.store.module.product.service;

import com.jupiter.store.module.product.model.ProductImage;
import com.jupiter.store.module.product.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public List<String> findByProductVariantId(Integer productVariantId) {
        List<ProductImage> productImages = productImageRepository.findByProductVariantId(productVariantId);
        if (productImages == null || productImages.isEmpty()) {
            throw new RuntimeException("No images found for product variant ID: " + productVariantId);
        }
        return productImages.stream().map(ProductImage::getImagePath).collect(Collectors.toList());
    }

    public void saveProductImages(Integer productVariantId, List<String> imagePaths) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String imagePath : imagePaths) {
                ProductImage productImage = new ProductImage();
                productImage.setProductVariantId(productVariantId);
                productImage.setImagePath(imagePath);
                productImageRepository.save(productImage);
            }
        }
    }

    public void updateProductImages(Integer productVariantId, List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return;
        }

        // Get existing images
        List<ProductImage> existingImages = productImageRepository.findByProductVariantId(productVariantId);
        Set<String> newImagePathsSet = new HashSet<>(imagePaths);
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
        for (String imagePath : imagePaths) {
            if (!existingImagePaths.contains(imagePath)) {
                ProductImage productImage = new ProductImage();
                productImage.setProductVariantId(productVariantId);
                productImage.setImagePath(imagePath);
                productImageRepository.save(productImage);
            }
        }
    }
}
