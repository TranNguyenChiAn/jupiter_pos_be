package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.model.ProductAttribute;
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

    public ProductAttribute addAttribute(String name) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName(name);
        return attributeRepository.save(productAttribute);
    }

    public List<ProductAttribute> search() {
        return attributeRepository.findAll();
    }

    public ProductAttribute searchById(Integer id) {
        return attributeRepository.findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
    }

    public void updateAttribute(Integer attributeId, String name) {
        ProductAttribute productAttribute = attributeRepository.findById(attributeId).orElseThrow(() -> new RuntimeException("Size not found"));
        productAttribute.setAttributeName(name);
        attributeRepository.save(productAttribute);
    }

    public void deleteAttribute(Integer id) {
        attributeRepository.deleteById(id);
    }
}
