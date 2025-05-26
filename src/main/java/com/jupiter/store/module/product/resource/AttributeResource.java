package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.dto.ProductAttributeDTO;
import com.jupiter.store.module.product.model.ProductAttribute;
import com.jupiter.store.module.product.service.AttributeService;
import com.jupiter.store.module.role.constant.RoleBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeResource {
    @Autowired
    private AttributeService attributeService;

    @PostMapping("/create")
    public ProductAttribute addAttribute(@RequestBody ProductAttributeDTO attributeDTO) {
        return attributeService.addAttribute(attributeDTO.getName());
    }

    @GetMapping("/search")
    public List<ProductAttribute> search() {
        return attributeService.search();
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public ProductAttribute searchById(@PathVariable Integer id) {
        return attributeService.searchById(id);
    }

    @GetMapping("/search-by-name")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public List<ProductAttribute> searchByName(@RequestParam String attributeName) {
        return attributeService.searchByName(attributeName);
    }

    @PutMapping("/update/{attributeId}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public void updateAttribute(@PathVariable Integer attributeId, @RequestParam String name) {
        attributeService.updateAttribute(attributeId, name);
    }

    @DeleteMapping("/delete/{attributeId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void deleteAttribute(@PathVariable Integer attributeId) {
        attributeService.deleteAttribute(attributeId);
    }
}
