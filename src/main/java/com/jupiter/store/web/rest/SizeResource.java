package com.jupiter.store.web.rest;

import com.jupiter.store.constant.RoleBase;
import com.jupiter.store.domain.Size;
import com.jupiter.store.dto.UpdateSizeDTO;
import com.jupiter.store.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
public class SizeResource {
    @Autowired
    private SizeService sizeService;

    @PostMapping("/add")
    @PreAuthorize("hasRole(RoleBase.ADMIN)")
    public void addCategory(String name) {
        sizeService.addSize(name);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole(RoleBase.ADMIN)")
    public List<Size> search() {
        return sizeService.search();
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public Size searchById(@RequestParam Long id) {
        return sizeService.searchById(id);
    }

    @PutMapping("/update/{sizeId}")
    @PreAuthorize("hasAuthority(RoleBase.ADMIN)")
    public void updateCategory(@RequestBody UpdateSizeDTO updateSizeDTO) {
        sizeService.updateSize(updateSizeDTO);
    }

    @DeleteMapping("/delete/{sizeId}")
    @PreAuthorize("hasAuthority(\"" + RoleBase.ADMIN + "\")")
    public void deleteCategory(@RequestParam Long id) {
        sizeService.deleteSize(id);
    }
}
