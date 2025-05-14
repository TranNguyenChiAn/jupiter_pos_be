package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.product.model.Unit;
import com.jupiter.store.module.product.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {
    Integer currentUserId = SecurityUtils.getCurrentUserId();
    @Autowired
    private UnitRepository unitRepository;

    public Unit createUnit(String name) {
        Unit unit = new Unit();
        unit.setName(name);
        return unitRepository.save(unit);
    }

    public List<Unit> findAllUnit() {
        return unitRepository.findAll();
    }

    public Unit updateUnit(Integer unitId, String name) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found"));
        unit.setName(name);
        return unitRepository.save(unit);
    }

    public void deleteUnit(Integer unitId) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found"));
        unitRepository.delete(unit);
    }
}
