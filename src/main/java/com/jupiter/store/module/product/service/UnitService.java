package com.jupiter.store.module.product.service;

import com.jupiter.store.module.product.model.Unit;
import com.jupiter.store.module.product.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;

    public Unit createUnit(String unitName) {
        if (unitRepository.existsByUnitName(unitName.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đơn vị " + unitName + " đã tồn tại!");
        }
        Unit unit = new Unit();
        unit.setName(unitName);
        return unitRepository.save(unit);
    }

    public List<Unit> findAllUnit() {
        return unitRepository.findAll();
    }

    public List<Unit> findUnitByName(String unitName) {
        List<Unit> units = unitRepository.findByUnitName(unitName.toLowerCase());
        if (units == null || units.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn vị!");
        }
        return units;
    }

    public Unit updateUnit(Integer unitId, String name) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn vị!"));

        if (unitRepository.existsByUnitName(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đơn vị " + name + " đã tồn tại!");
        }

        unit.setName(name);
        return unitRepository.save(unit);
    }

    public void deleteUnit(Integer unitId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn vị!"));
        unitRepository.delete(unit);
    }
}
