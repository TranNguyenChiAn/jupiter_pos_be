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
    public Unit createUnit(String unitName) {
        return unitService.createUnit(unitName);
    }

    @GetMapping("/find-all")
    public List<Unit> findAllUnit() {
        return unitService.findAllUnit();
    }

    @GetMapping("/find-by-name")
    public List<Unit> findUnitByName(@RequestParam String unitName) {
        return unitService.findUnitByName(unitName);
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
