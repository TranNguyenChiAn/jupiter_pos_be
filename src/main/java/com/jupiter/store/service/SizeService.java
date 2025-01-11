package com.jupiter.store.service;

import com.jupiter.store.domain.Size;
import com.jupiter.store.dto.UpdateSizeDTO;
import com.jupiter.store.repository.SizeRepository;
import com.jupiter.store.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    public void addSize(String name) {
        Size size = new Size();
        size.setSizeName(name);
        size.setCreatedBy(3481888888888888L);
        sizeRepository.save(size);
    }

    public List<Size> search() {
        return sizeRepository.findAll();
    }

    public Size searchById(Long id) {
        return sizeRepository.findById(id).orElseThrow(() -> new RuntimeException("Size not found"));
    }

    public void updateSize(UpdateSizeDTO updateSizeDTO) {
        Size size = sizeRepository.findById(updateSizeDTO.getId()).orElseThrow(() -> new RuntimeException("Size not found"));
        size.setSizeName(updateSizeDTO.getName());
        size.setLastModifiedBy(3481888888888888L);
        size.setLastModifiedDate(LocalDateTime.now());
        sizeRepository.save(size);
    }

    public void deleteSize(Long id) {
        sizeRepository.deleteById(id);
    }
}
