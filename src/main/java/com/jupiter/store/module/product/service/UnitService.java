package com.jupiter.store.module.product.service;

import com.jupiter.store.common.utils.HelperUtils;
import com.jupiter.store.module.product.model.Unit;
import com.jupiter.store.module.product.repository.ProductVariantAttrValueRepository;
import com.jupiter.store.module.product.repository.ProductVariantRepository;
import com.jupiter.store.module.product.repository.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private ProductVariantAttrValueRepository productVariantAttrValueRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private HelperUtils helperUtils;

    public Unit createUnit(String unitName) {
        if (unitRepository.existsByUnitName(unitName.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đơn vị " + unitName + " đã tồn tại!");
        }
        Unit unit = new Unit();
        unit.setName(unitName);
        return unitRepository.save(unit);
    }

    public Page<Unit> search(String search, int page, int size, String sortBy, String sortDirection) {
        helperUtils.validatePageAndSize(page, size);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        search = helperUtils.normalizeSearch(search);
        return unitRepository.search(search, pageable);
    }

    public List<Unit> findAllUnit() {
        Sort sort = Sort.by(Sort.Direction.fromString("DESC"), "id");
        Pageable pageable = PageRequest.of(0, 99999, sort);
        Page<Unit> page = unitRepository.findAll(pageable);
        return page.getContent();
    }

    public Unit findById(Integer unitId) {
        if (unitId == null) {
            log.debug("Unit ID is null, returning null.");
            return null;
        }
        return unitRepository.findById(unitId)
                .orElse(null);
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
        productVariantAttrValueRepository.updateUnitIdNull(unitId);
        productVariantRepository.updateUnitIdNull(unitId);
        unitRepository.delete(unit);
    }
}
