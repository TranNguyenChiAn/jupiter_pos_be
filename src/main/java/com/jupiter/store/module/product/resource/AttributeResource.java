package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.model.Attribute;
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
    @PreAuthorize("hasRole(RoleBase.ADMIN)")
    public Attribute addAttribute(String name) {
        return attributeService.addAttribute(name);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole(RoleBase.ADMIN)")
    public List<Attribute> search() {
        return attributeService.search();
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public Attribute searchById(@RequestParam Long id) {
        return attributeService.searchById(id);
    }

    @PutMapping("/update/{attributeId}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public void updateAttribute(@RequestParam Long attributeId, @RequestParam String name) {
        attributeService.updateAttribute(attributeId, name);
    }

    @DeleteMapping("/delete/{attributeId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void deleteAttribute(@RequestParam Long id) {
        attributeService.deleteAttribute(id);
    }
}
