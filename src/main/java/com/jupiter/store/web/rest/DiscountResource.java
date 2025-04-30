package com.jupiter.store.web.rest;

import com.jupiter.store.model.Discount;
import com.jupiter.store.dto.DiscountDTO;
import com.jupiter.store.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountResource {
    @Autowired
    private DiscountService discountService;

    @PostMapping("/create-voucher")
    public Discount createVoucher(@RequestBody DiscountDTO discountDTO) {
        return discountService.createVoucher(discountDTO);
    }

    @PutMapping("/update-voucher/{discountId}")
    public ResponseEntity<Discount> updateVoucher(@PathVariable Long discountId , @RequestBody DiscountDTO discountDTO) {
        return discountService.updateVoucher(discountId, discountDTO);
    }

    @PutMapping("/update-voucher-by-productId/{productId}")
    public ResponseEntity<Discount> updateVoucherByProductId(@PathVariable Long productId, @RequestParam double percentage) {
        return discountService.updateVoucherByProductId(productId, percentage);
    }
}
