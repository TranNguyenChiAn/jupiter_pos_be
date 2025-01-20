package com.jupiter.store.web.rest;

import com.jupiter.store.domain.Size;
import com.jupiter.store.dto.UpdateSizeDTO;
import com.jupiter.store.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes")
public class SizeResource {
    @Autowired
    private SizeService sizeService;

    @PostMapping("/add")
    public void addCategory(String name) {
        sizeService.addSize(name);
    }

    @GetMapping("/search")
    public List<Size> search() {
        return sizeService.search();
    }

    @GetMapping("/search/{id}")
    public Size searchById(@RequestParam Long id) {
        return sizeService.searchById(id);
    }

    @PutMapping("/update/{sizeId}")
    public void updateCategory(@RequestBody UpdateSizeDTO updateSizeDTO) {
        sizeService.updateSize(updateSizeDTO);
    }

    @DeleteMapping("/delete/{sizeId}")
    public void deleteCategory(@RequestParam Long id) {
        sizeService.deleteSize(id);
    }
}
