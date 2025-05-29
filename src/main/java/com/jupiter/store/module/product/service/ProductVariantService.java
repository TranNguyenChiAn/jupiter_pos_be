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
                        ProductAttribute attribute = attributeService.searchById(attributeValue.getAttrId());
                        if (attribute != null) {
                            productVariantAttrValueDto.setAttrId(attributeValue.getAttrId());
                            productVariantAttrValueDto.setAttrName(attribute.getAttributeName());
                        }
                        if (attributeValue.getAttrValue() == null) {
                            productVariantAttrValueDto.setAttrValue("");
                        } else {
                            productVariantAttrValueDto.setAttrValue(attributeValue.getAttrValue());
                        }
                        Unit unit = unitService.findById(attributeValue.getUnitId());
                        if (unit != null) {
                            productVariantAttrValueDto.setUnitId(attributeValue.getUnitId());
                            productVariantAttrValueDto.setUnitName(unit.getName());
                        } else {
                            productVariantAttrValueDto.setUnitId(null);
                            productVariantAttrValueDto.setUnitName(null);
                        }
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
        Long costPrice = newProductVariant.getCostPrice();
        if (costPrice == null) {
            costPrice = 0L; // Default to 0 if costPrice is null
        }
        variant.setCostPrice(costPrice < 0 ? 0L : costPrice); // Ensure costPrice is not negative
        Long price = newProductVariant.getPrice();
        if (price == null) {
            price = 0L; // Default to 0 if price is null
        }
        variant.setPrice(price < 0 ? 0L : price); // Ensure price is not negative
        Integer quantity = newProductVariant.getQuantity();
        if (quantity == null) {
            quantity = 0; // Default to 0 if quantity is null
        }
        variant.setQuantity(quantity < 0 ? 0 : quantity); // Ensure quantity is not negative
        Integer unitId = newProductVariant.getUnitId();
        variant.setUnitId(unitId);
        variant.setSku(newProductVariant.getSku());
        variant.setBarcode(newProductVariant.getBarcode());
        variant.setExpiryDate(newProductVariant.getExpiryDate());
        variant.setStatus(newProductVariant.getStatus());
        variant.setLastModifiedBy(SecurityUtils.getCurrentUserId());
        variant.setLastModifiedDate(LocalDateTime.now());
        productVariantRepository.save(variant);
        if (newProductVariant.getImagePaths() != null && !newProductVariant.getImagePaths().isEmpty()) {
            updateProductImages(variantId, newProductVariant.getImagePaths());
        }
//        else {
//            List<ProductImage> productImages = productImageRepository.findByProductVariantId(variantId);
//            variant.setIm
//        }

        if (newProductVariant.getAttrAndValues() != null && !newProductVariant.getAttrAndValues().isEmpty()) {
            // Delete all existing attribute values for the variant
            List<ProductAttributeValue> existingValues = productVariantAttrValueRepository.findByProductVariantId(variantId);
            if (existingValues != null && !existingValues.isEmpty()) {
                productVariantAttrValueRepository.deleteAll(existingValues);
            }
            // Save new attribute values
            List<ProductAttributeValue> newValues = newProductVariant.getAttrAndValues().stream()
                    .map(attrValue -> {
                        ProductAttributeValue value = new ProductAttributeValue();
                        value.setProductVariantId(variantId);
                        value.setAttrId(attrValue.getAttrId());
                        value.setAttrValue(attrValue.getAttrValue());
                        value.setUnitId(attrValue.getUnitId());
                        // Set additional fields if needed
                        return value;
                    }).collect(Collectors.toList());
            productVariantAttrValueRepository.saveAll(newValues);
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
