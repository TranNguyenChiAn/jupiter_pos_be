package com.jupiter.store.module.product.service;

import com.jupiter.store.module.product.model.ProductAttribute;
import com.jupiter.store.module.product.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepository attributeRepository;

    public ProductAttribute addAttribute(String attributeName) {
        if (attributeRepository.existsByAttributeName(attributeName.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên thuộc tính " + attributeName + " đã tồn tại!");
        }
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName(attributeName);
        return attributeRepository.save(productAttribute);
    }

    public List<ProductAttribute> search() {
        return attributeRepository.findAll();
    }

    public ProductAttribute searchById(Integer id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thuộc tính!"));
    }

    public List<ProductAttribute> searchByName(String attributeName) {
        List<ProductAttribute> attributes = attributeRepository.findByAttributeName(attributeName.toLowerCase());
        if (attributes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuộc tính với tên " + attributeName);
        }
        return attributes;
    }

    public void updateAttribute(Integer attributeId, String name) {
        ProductAttribute productAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thuộc tính!"));
        if (attributeRepository.existsByAttributeName(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên thuộc tính " + name + " đã tồn tại!");
        }
        productAttribute.setAttributeName(name);
        attributeRepository.save(productAttribute);
    }

    public void deleteAttribute(Integer attributeId) {
        ProductAttribute productAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy thuộc tính!"));
        attributeRepository.delete(productAttribute);
    }
}
