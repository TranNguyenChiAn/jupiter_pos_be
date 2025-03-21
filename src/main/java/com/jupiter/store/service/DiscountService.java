package com.jupiter.store.service;

import com.jupiter.store.model.Discount;
import com.jupiter.store.model.ProductDiscount;
import com.jupiter.store.dto.DiscountDTO;
import com.jupiter.store.repository.DiscountRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount createVoucher(DiscountDTO discountDTO) {
        List<Long> productId = discountDTO.getProductId();
        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartAt(discountDTO.getStartAt());
        discount.setEndAt(discountDTO.getEndAt());
        discount.setActive(discountDTO.isActive());
        discount.setCreatedBy(3481888888888888L);
        discountRepository.save(discount);

        for (Long id : productId) {
            ProductDiscount productDiscount = new ProductDiscount();
            productDiscount.setDiscountId(discount.getId());
            productDiscount.setProductId(id);
        }
        return discount;
    }

    public ResponseEntity<Discount> updateVoucher(Long discountId, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Discount not found"));

        discount.setName(discountDTO.getName());
        discount.setPercentage(discountDTO.getPercentage());
        discount.setStartAt(discountDTO.getStartAt());
        discount.setEndAt(discountDTO.getEndAt());
        discount.setActive(discountDTO.isActive());
        return ResponseEntity.ok(discountRepository.save(discount));
    }

    public ResponseEntity<Discount> updateVoucherByProductId(Long productId, double percentage) {
        Discount discount = discountRepository.findByProductId(productId)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Discount not found"));
        discount.setPercentage(percentage);
        return ResponseEntity.ok(discountRepository.save(discount));
    }
}
