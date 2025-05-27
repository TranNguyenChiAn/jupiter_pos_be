package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.dto.*;
import com.jupiter.store.module.product.model.*;
import com.jupiter.store.module.product.repository.ProductImageRepository;
import com.jupiter.store.module.product.repository.ProductRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private UnitService unitService;

    public List<GetAllProductVariantDTO> searchProductVariant(Integer productId) {
        List<ProductVariant> productVariants = productVariantRepository.findByProductId(productId);

        if (productVariants.isEmpty()) {
            throw new OpenApiResourceNotFoundException("Product variant not found with ID: " + productId);
        }

        return productVariants.stream()
                .map(productVariant -> {
                    // Return product variant details
                    GetAllProductVariantDTO getProductVariantsDTO = new GetAllProductVariantDTO();
                    getProductVariantsDTO.setProductVariantId(productVariant.getId());
                    getProductVariantsDTO.setCostPrice(productVariant.getCostPrice());
                    getProductVariantsDTO.setPrice(productVariant.getPrice());
                    getProductVariantsDTO.setQuantity(productVariant.getQuantity());
                    getProductVariantsDTO.setSku(productVariant.getSku());
                    getProductVariantsDTO.setBarcode(productVariant.getBarcode());
                    getProductVariantsDTO.setExpiryDate(productVariant.getExpiryDate());
                    getProductVariantsDTO.setStatus(productVariant.getStatus());

                    // Trả về thông tin về thuộc tính và giá trị của thuộc tính
                    List<ProductAttributeValue> productAttributeValues = productVariantAttrValueRepository.findByProductVariantId(productVariant.getId());
                    List<ProductVariantAttrValueDTO> attributeValues = new ArrayList<>();
                    for (ProductAttributeValue attributeValue : productAttributeValues) {
                        ProductVariantAttrValueDTO productVariantAttrValueDto = new ProductVariantAttrValueDTO();
                        productVariantAttrValueDto.setAttrId(attributeValue.getAttrId());
                        productVariantAttrValueDto.setAttrValue(attributeValue.getAttrValue());
                        productVariantAttrValueDto.setUnitId(attributeValue.getUnitId());
                        attributeValues.add(productVariantAttrValueDto);
                    }
                    getProductVariantsDTO.setAttrAndValues(attributeValues);

                    // Trả về ảnh của product variant
                    List<ProductImage> productImages = productImageRepository.findByProductVariantId(productVariant.getId());
                    // Set image paths for the attribute value
                    List<String> imagePaths = new ArrayList<>();
                    for (ProductImage image : productImages) {
                        if (image.getProductVariantId().equals(productVariant.getId())) {
                            imagePaths.add(image.getImagePath());
                        }
                    }
                    getProductVariantsDTO.setImagePaths(imagePaths);
                    return getProductVariantsDTO;
                }).collect(Collectors.toList());
    }

    public List<ProductVariantReadDTO> searchVariant(Integer productId) {
        List<ProductVariant> productVariants = productVariantRepository.findByProductId(productId);
        return productVariants.stream()
                .map(productVariant -> {
                    // Return product variant details
                    ProductVariantReadDTO productVariantReadDTO = new ProductVariantReadDTO(productVariant);

                    // Trả về thông tin về thuộc tính và giá trị của thuộc tính
                    List<ProductAttributeValue> productAttributeValues = productVariantAttrValueRepository.findByProductVariantId(productVariant.getId());
                    List<ProductVariantAttrValueSimpleReadDTO> attributeValues = new ArrayList<>();
                    for (ProductAttributeValue attributeValue : productAttributeValues) {
                        ProductVariantAttrValueSimpleReadDTO productVariantAttrValueDto = new ProductVariantAttrValueSimpleReadDTO();
                        productVariantAttrValueDto.setAttrName(attributeService.searchById(attributeValue.getAttrId()).getAttributeName());
                        productVariantAttrValueDto.setAttrValue(attributeValue.getAttrValue());
                        Unit unit = unitService.findById(attributeValue.getUnitId());
                        productVariantAttrValueDto.setUnitName(unit != null ? unit.getName() : null);
                        attributeValues.add(productVariantAttrValueDto);
                    }
                    productVariantReadDTO.setAttrValues(attributeValues);

                    // Trả về ảnh của product variant
                    List<ProductImage> productImages = productImageRepository.findByProductVariantId(productVariant.getId());
                    // Set image paths for the attribute value
                    List<String> imagePaths = new ArrayList<>();
                    for (ProductImage image : productImages) {
                        if (image.getProductVariantId().equals(productVariant.getId())) {
                            imagePaths.add(image.getImagePath());
                        }
                    }
                    productVariantReadDTO.setImagePaths(imagePaths);
                    return productVariantReadDTO;
                }).collect(Collectors.toList());
    }

    public ResponseEntity<CreateProductVariantDTO> addProductVariant(Integer productId, CreateProductVariantDTO productVariant) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
        if (product != null) {
            Integer currentUserId = SecurityUtils.getCurrentUserId();
            ProductVariant variant = new ProductVariant(null, productId, productVariant.getCostPrice(),
                    productVariant.getPrice(), productVariant.getQuantity(),
                    productVariant.getUnitId(), productVariant.getSku(),
                    productVariant.getBarcode(), productVariant.getExpiryDate(),
                    productVariant.getStatus());
            variant = setAuditFields(variant, true);
            productVariantRepository.save(variant);

            saveProductImages(variant.getId(), productVariant.getImagePaths());

            ProductVariant finalVariant = variant;
            List<ProductAttributeValue> attributeValues = productVariant.getAttrAndValues().stream()
                    .map(attrValue -> new ProductAttributeValue(finalVariant.getId(), attrValue, currentUserId))
                    .collect(Collectors.toList());
            productVariantAttrValueRepository.saveAll(attributeValues);
            return ResponseEntity.ok(productVariant);
        } else {
            throw new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + productId);
        }
    }

    public ResponseEntity<CreateProductVariantDTO> updateProductVariant(Integer variantId, CreateProductVariantDTO newProductVariant) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new RuntimeException("Product variant not found"));
        variant.setCostPrice(newProductVariant.getCostPrice() != 0 ? newProductVariant.getCostPrice() : variant.getCostPrice());
        variant.setPrice(newProductVariant.getPrice() != 0 ? newProductVariant.getPrice() : variant.getPrice());
        variant.setQuantity(newProductVariant.getQuantity() != 0 ? newProductVariant.getQuantity() : variant.getQuantity());
        variant.setUnitId(newProductVariant.getUnitId() != 0 ? newProductVariant.getUnitId() : variant.getUnitId());
        variant.setSku(newProductVariant.getSku() != null ? newProductVariant.getSku() : variant.getSku());
        variant.setBarcode(newProductVariant.getBarcode() != null ? newProductVariant.getBarcode() : variant.getBarcode());
        variant.setExpiryDate(newProductVariant.getExpiryDate() != null ? newProductVariant.getExpiryDate() : variant.getExpiryDate());
        variant.setStatus(newProductVariant.getStatus() != null ? newProductVariant.getStatus() : variant.getStatus());
        variant.setLastModifiedBy(SecurityUtils.getCurrentUserId());
        productVariantRepository.save(variant);
        if (newProductVariant.getImagePaths() != null && !newProductVariant.getImagePaths().isEmpty()) {
            updateProductImages(variantId, newProductVariant.getImagePaths());
        }
//        else {
//            List<ProductImage> productImages = productImageRepository.findByProductVariantId(variantId);
//            variant.setIm
//        }

        if (newProductVariant.getAttrAndValues() == null || newProductVariant.getAttrAndValues().isEmpty()) {
            for (ProductVariantAttrValueDTO attrValue : newProductVariant.getAttrAndValues()) {
                ProductAttributeValue productAttributeValue = productVariantAttrValueRepository.findByProductIdAndAttrId(variantId, attrValue.getAttrId())
                        .orElseThrow(() -> new RuntimeException("Product variant attribute value not found"));
                productAttributeValue.setAttrValue(attrValue.getAttrValue());
                productVariantAttrValueRepository.save(productAttributeValue);
            }
        }

        return ResponseEntity.ok(newProductVariant);
    }

    private void saveProductImages(Integer productVariantId, List<String> imagePaths) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String imagePath : imagePaths) {
                ProductImage productImage = new ProductImage();
                productImage.setProductVariantId(productVariantId);
                productImage.setImagePath(imagePath);
                productImageRepository.save(productImage);
            }
        }
    }

    private void updateProductImages(Integer productVariantId, List<String> imagePaths) {
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

    public void deleteProductVariant(Integer productVariantId) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productVariantId));
        List<ProductAttributeValue> productAttributeValue = productVariantAttrValueRepository.findByProductVariantId(productVariantId);
        List<ProductImage> productImages = productImageRepository.findByProductVariantId(productVariantId);
        if (productAttributeValue != null && !productAttributeValue.isEmpty()) {
            productVariantAttrValueRepository.deleteAll(productAttributeValue);
        }
        if (productImages != null && !productImages.isEmpty()) {
            productImageRepository.deleteAll(productImages);
        }
        productVariantRepository.delete(productVariant);

    }

    private ProductVariant setAuditFields(ProductVariant variant, Boolean isCreate) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (isCreate) {
            variant.setCreatedBy(currentUserId);
            variant.setCreatedDate(LocalDateTime.now());
        }
        variant.setLastModifiedBy(currentUserId);
        variant.setLastModifiedDate(LocalDateTime.now());
        return variant;
    }
}
