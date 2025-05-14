package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.model.ProductAttributeType;
import com.jupiter.store.module.product.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepository attributeRepository;

    public static Integer currentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public ProductAttributeType addAttribute(String name) {
        ProductAttributeType productAttributeType = new ProductAttributeType();
        productAttributeType.setName(name);
        productAttributeType.setCreatedBy(currentUserId());
        return attributeRepository.save(productAttributeType);
    }

    public List<ProductAttributeType> search() {
        return attributeRepository.findAll();
    }

    public ProductAttributeType searchById(Integer id) {
        return attributeRepository.findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
    }

    public void updateAttribute(Integer attributeId, String name) {
        ProductAttributeType productAttributeType = attributeRepository.findById(attributeId).orElseThrow(() -> new RuntimeException("Size not found"));
        productAttributeType.setName(name);
        productAttributeType.setLastModifiedBy(currentUserId());
        productAttributeType.setLastModifiedDate(LocalDateTime.now());
        attributeRepository.save(productAttributeType);
    }

    public void deleteAttribute(Integer id) {
        attributeRepository.deleteById(id);
    }
}
