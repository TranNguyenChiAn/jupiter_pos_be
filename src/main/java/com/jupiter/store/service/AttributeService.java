package com.jupiter.store.service;

import com.jupiter.store.model.Attribute;
import com.jupiter.store.repository.AttributeRepository;
import com.jupiter.store.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepository attributeRepository;
    public static Long currentUserId(){
        return SecurityUtils.getCurrentUserId();
    }

    public Attribute addAttribute(String name) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setCreatedBy(currentUserId());
        return attributeRepository.save(attribute);
    }

    public List<Attribute> search() {
        return attributeRepository.findAll();
    }

    public Attribute searchById(Long id) {
        return attributeRepository.findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
    }

    public void updateAttribute(Long attributeId, String name) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new RuntimeException("Size not found"));
        attribute.setName(name);
        attribute.setLastModifiedBy(currentUserId());
        attribute.setLastModifiedDate(LocalDateTime.now());
        attributeRepository.save(attribute);
    }

    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }
}
