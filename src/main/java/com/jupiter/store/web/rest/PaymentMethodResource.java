package com.jupiter.store.web.rest;

import com.jupiter.store.domain.PaymentMethod;
import com.jupiter.store.dto.CreatePaymentMethodDTO;
import com.jupiter.store.dto.UpdatePaymentMethodDTO;
import com.jupiter.store.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodResource {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    public PaymentMethod createPaymentMethod(@RequestBody CreatePaymentMethodDTO createPaymentMethodDTO) {
        return paymentMethodService.createPaymentMethod(createPaymentMethodDTO);
    }


    @GetMapping("/{id}")
    public PaymentMethod getPaymentMethod(@PathVariable Long id) {
       return paymentMethodService.findPaymentMethodById(id);
    }

    @GetMapping("/all")
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodService.findAllPaymentMethods();
    }

    @PutMapping("/update")
    public PaymentMethod updatePaymentMethod(@RequestBody UpdatePaymentMethodDTO updatePaymentMethodDTO) {
        return paymentMethodService.updatePaymentMethod(updatePaymentMethodDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
    }
}
