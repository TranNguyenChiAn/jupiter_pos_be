package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.model.Unit;
import com.jupiter.store.module.product.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitResource {
    @Autowired
    private UnitService unitService;

    @PostMapping("/create")
    public Unit createUnit(String name) {
        return unitService.createUnit(name);
    }

    @GetMapping("/find-all")
    public List<Unit> findAllUnit(Integer id, String name) {
        return unitService.findAllUnit(id, name);
    }

    @PutMapping("/update/{unitId}")
    public Unit updateUnit(@PathVariable Integer unitId, String name) {
        return unitService.updateUnit(unitId, name);
    }

    @DeleteMapping("/delete/{unitId}")
    public void deleteUnit(@PathVariable Integer unitId) {
        unitService.deleteUnit(unitId);
    }
}
