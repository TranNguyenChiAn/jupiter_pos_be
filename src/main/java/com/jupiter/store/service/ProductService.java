package com.jupiter.store.service;

import com.jupiter.store.domain.*;
import com.jupiter.store.dto.CreateProductDTO;
import com.jupiter.store.dto.ProductVariantDTO;
import com.jupiter.store.dto.UpdateProductDTO;
import com.jupiter.store.repository.ProductCategoryRepository;
import com.jupiter.store.repository.ProductImageRepository;
import com.jupiter.store.repository.ProductRepository;
import com.jupiter.store.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductVariantRepository productVariantRepository;

    public void search() {
        productRepository.findAll();
    }

    public void searchById(Long id) {
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void addProduct(CreateProductDTO createProductDTO) {
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setMaterial(createProductDTO.getMaterial());
        product.setCreatedBy(3481888888888888L);
        productRepository.save(product);
        Product savedProduct = productRepository.save(product);

        List<String> productImages = createProductDTO.getImagePath();
        if (!productImages.isEmpty()) {
            for (String image : productImages) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(savedProduct.getId());
                productImage.setImagePath(image);
                productImage.setCreatedBy(3481888888888888L);
                productImageRepository.save(productImage);
            }
        }

        List<Long> productCategories = createProductDTO.getCategoryId();
        if(!productCategories.isEmpty()) {
            for (Long categoryId : productCategories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductId(savedProduct.getId());
                productCategory.setCategoryId(categoryId);
                productCategory.setCreatedBy(3481888888888888L);
                productCategoryRepository.save(productCategory);
            }
        }

        List<ProductVariantDTO> variants = createProductDTO.getVariants();
        if (!variants.isEmpty()) {
            for (ProductVariantDTO variantDTO : variants) {
                ProductVariant variant = new ProductVariant();
                variant.setProductId(savedProduct.getId());
                variant.setPrice(variantDTO.getPrice());
                variant.setQuantity(variantDTO.getQuantity());
                variant.setColor(variantDTO.getColor());
                variant.setSizeId(variantDTO.getSizeId());
                variant.setImagePath(variantDTO.getImagePath());
                variant.setCreatedBy(3481888888888888L);
                productVariantRepository.save(variant);
            }
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductDTO updateProductDTO) {
        Optional<Product> existingProductOptional = productRepository.findById(productId);
        if (!existingProductOptional.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        Product product = existingProductOptional.get();

        product.setName(updateProductDTO.getName());
        product.setDescription(updateProductDTO.getDescription());
        product.setMaterial(updateProductDTO.getMaterial());
        productRepository.save(product);

       // Cập nhật ProductImages
        List<String> productImages = updateProductDTO.getImagePath();
        if (productImages != null && !productImages.isEmpty()) {
            // Lấy danh sách ảnh cũ
            List<ProductImage> existingImages = productImageRepository.findByProductId(productId);

            // Tạo Set các imagePath mới từ request
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

        // Cập nhật ProductCategories
        List<Long> productCategories = updateProductDTO.getCategoryId();
        if (productCategories != null && !productCategories.isEmpty()) {
            // Lấy danh sách các productCategory cũ
            List<ProductCategory> existingCategories = productCategoryRepository.findByProductId(productId);

            // Tạo Set các categoryId mới từ request
            Set<Long> newCategoryIds = new HashSet<>(productCategories);

            // Xóa các category cũ không có trong request (bị xóa)
            for (ProductCategory existingCategory : existingCategories) {
                if (!newCategoryIds.contains(existingCategory.getCategoryId())) {
                    productCategoryRepository.delete(existingCategory);
                }
            }

            // Thêm category mới hoặc cập nhật
            for (Long categoryId : productCategories) {
                Optional<ProductCategory> existingCategory = existingCategories.stream()
                        .filter(cat -> cat.getCategoryId().equals(categoryId))
                        .findFirst();

                if (existingCategory.isEmpty()) {  // Nếu category mới không có trong DB, thêm mới
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setProductId(product.getId());
                    productCategory.setCategoryId(categoryId);
                    productCategory.setCreatedBy(3481888888888888L);
                    productCategoryRepository.save(productCategory);
                }
            }
        }

        // Cập nhật ProductVariants
        List<ProductVariantDTO> variants = updateProductDTO.getVariants();
        if (variants != null && !variants.isEmpty()) {
            // Lấy danh sách các productVariants cũ
            List<ProductVariant> existingVariants = productVariantRepository.findByProductId(productId);

            // Tạo Set các variant mới từ request
            Set<String> newVariantKeys = new HashSet<>();
            for (ProductVariantDTO variantDTO : variants) {
                newVariantKeys.add(variantDTO.getColor() + "_" + variantDTO.getSizeId());
            }

            // Xóa các variant cũ không có trong request (bị xóa)
            for (ProductVariant existingVariant : existingVariants) {
                String variantKey = existingVariant.getColor() + "_" + existingVariant.getSizeId();
                if (!newVariantKeys.contains(variantKey)) {
                    productVariantRepository.delete(existingVariant);
                }
            }

            // Thêm variant mới hoặc cập nhật
            for (ProductVariantDTO variantDTO : variants) {
                String variantKey = variantDTO.getColor() + "_" + variantDTO.getSizeId();

                Optional<ProductVariant> existingVariant = existingVariants.stream()
                        .filter(variant -> variant.getColor().equals(variantDTO.getColor()) &&
                                variant.getSizeId().equals(variantDTO.getSizeId()))
                        .findFirst();

                if (existingVariant.isEmpty()) {  // Nếu variant mới không có trong DB, thêm mới
                    ProductVariant variant = new ProductVariant();
                    variant.setProductId(product.getId());
                    variant.setPrice(variantDTO.getPrice());
                    variant.setQuantity(variantDTO.getQuantity());
                    variant.setColor(variantDTO.getColor());
                    variant.setSizeId(variantDTO.getSizeId());
                    variant.setImagePath(variantDTO.getImagePath());
                    variant.setCreatedBy(3481888888888888L);
                    productVariantRepository.save(variant);
                }
            }
        }
//
    }
}
