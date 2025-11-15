package com.jupiter.store.module.product.resource;

import com.jupiter.store.module.product.dto.UnitDTO;
import com.jupiter.store.module.product.dto.UnitSearchDTO;
import com.jupiter.store.module.product.model.Unit;
import com.jupiter.store.module.product.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitResource {
    @Autowired
    private UnitService unitService;

    @Operation(summary = "Tạo đơn vị mới cho sản phẩm")
    @PostMapping("/create")
    public Unit createUnit(@RequestBody UnitDTO unitDTO) {
        return unitService.createUnit(unitDTO.getName());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Unit>> search(@RequestBody UnitSearchDTO request) {
        Page<Unit> result = unitService.search(
                request.getSearch(),
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-all")
    public List<Unit> findAllUnit() {
        return unitService.findAllUnit();
    }

    @GetMapping("/find-by-name")
    public List<Unit> findUnitByName(@RequestParam String name) {
        return unitService.findUnitByName(name);
    }

    @PutMapping("/{unitId}")
    public Unit updateUnit(@PathVariable Integer unitId, @RequestBody String name) {
        return unitService.updateUnit(unitId, name);
    }

    @DeleteMapping("/{unitId}")
    public void deleteUnit(@PathVariable Integer unitId) {
        unitService.deleteUnit(unitId);
    }
}
