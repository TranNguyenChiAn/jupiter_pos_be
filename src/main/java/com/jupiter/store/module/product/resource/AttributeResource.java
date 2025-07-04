package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.dto.ProductAttributeDTO;
import com.jupiter.store.module.product.dto.ProductAttributeSearchDTO;
import com.jupiter.store.module.product.model.ProductAttribute;
import com.jupiter.store.module.product.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeResource {
    @Autowired
    private AttributeService attributeService;

    @PostMapping("/create")
    public ProductAttribute addAttribute(@RequestBody ProductAttributeDTO attributeDTO) {
        return attributeService.addAttribute(attributeDTO.getAttributeName());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProductAttribute>> search(@RequestBody ProductAttributeSearchDTO request) {
        Page<ProductAttribute> result = attributeService.search(
                request.getSearch(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public List<ProductAttribute> getAll() {
        return attributeService.findAll();
    }

    @GetMapping("/search/{id}")
    public ProductAttribute searchById(@PathVariable Integer id) {
        return attributeService.searchById(id);
    }

    @GetMapping("/search-by-name")
    public List<ProductAttribute> searchByName(@RequestParam String attributeName) {
        return attributeService.searchByName(attributeName);
    }

    @PutMapping("/{attributeId}")
    public ProductAttribute updateAttribute(@PathVariable Integer attributeId, @RequestBody String attributeName) {
        return attributeService.updateAttribute(attributeId, attributeName);
    }

    @DeleteMapping("/{attributeId}")
    public void deleteAttribute(@PathVariable Integer attributeId) {
        attributeService.deleteAttribute(attributeId);
    }
}
