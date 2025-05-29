package com.jupiter.store.module.product.service;

import com.jupiter.store.module.product.dto.ProductVariantAttrValueSimpleReadDTO;
import com.jupiter.store.module.product.model.ProductAttribute;
import com.jupiter.store.module.product.repository.AttributeRepository;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeService {
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuộc tính!"));
    }

    public List<ProductAttribute> searchByName(String attributeName) {
        List<ProductAttribute> attributes = attributeRepository.findByAttributeName(attributeName.toLowerCase());
        if (attributes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuộc tính với tên " + attributeName);
        }
        return attributes;
    }

    public List<ProductVariantAttrValueSimpleReadDTO> findByVariantId(Integer variantId, Integer numberOfAttributes) {
        List<Object[]> result;
        if (numberOfAttributes != null) {
            result = productVariantAttrValueRepository.findTopAttributesByVariantId(variantId, numberOfAttributes);
        } else {
            result = productVariantAttrValueRepository.findAllAttributesByVariantId(variantId);
        }
        return mapToDTO(result);
    }

    public List<ProductVariantAttrValueSimpleReadDTO> mapToDTO(List<Object[]> rows) {
        return rows.stream()
                .map(r -> new ProductVariantAttrValueSimpleReadDTO(
                        (Integer) r[0], // attribute_id
                        (String) r[1],  // attribute_name
                        (String) r[2],   // attr_value
                        (Integer) r[3],   // unit_id
                        (String) r[4]   // unit_name
                ))
                .collect(Collectors.toList());
    }

    public void updateAttribute(Integer attributeId, String name) {
        ProductAttribute productAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuộc tính!"));
        if (attributeRepository.existsByAttributeName(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên thuộc tính " + name + " đã tồn tại!");
        }
        productAttribute.setAttributeName(name);
        attributeRepository.save(productAttribute);
    }

    public void deleteAttribute(Integer attributeId) {
        ProductAttribute productAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuộc tính!"));
        attributeRepository.delete(productAttribute);
    }
}
