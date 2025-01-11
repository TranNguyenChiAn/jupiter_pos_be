package com.jupiter.store.service;

import com.jupiter.store.domain.Discount;
import com.jupiter.store.dto.CreateDiscountDTO;
import com.jupiter.store.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public Discount addDiscount(CreateDiscountDTO createDiscountDTO) {
        com.jupiter.store.domain.Discount discount = new com.jupiter.store.domain.Discount();
        discount.setName(createDiscountDTO.getName());
        discount.setPercentage(createDiscountDTO.getPercentage());
        discount.setExpiredAt(createDiscountDTO.getExpiredDate());
        return discountRepository.save(discount);
    }

}
