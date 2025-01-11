package com.jupiter.store.service;

import com.jupiter.store.domain.PaymentMethod;
import com.jupiter.store.dto.CreatePaymentMethodDTO;
import com.jupiter.store.dto.UpdatePaymentMethodDTO;
import com.jupiter.store.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod createPaymentMethod(CreatePaymentMethodDTO createPaymentMethodDTO) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod(createPaymentMethodDTO.getMethod());
        paymentMethod.setCreatedBy(3481888888888888L);
        paymentMethod.setCreatedDate(LocalDateTime.now());
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod findPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment method not found"));
    }

    public List<PaymentMethod> findAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public void deletePaymentMethod(Long id) {
        paymentMethodRepository.deleteById(id);
    }

    public PaymentMethod updatePaymentMethod(UpdatePaymentMethodDTO updatePaymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(updatePaymentMethodDTO.getId()).orElseThrow(() -> new RuntimeException("Payment method not found"));
        paymentMethod.setMethod(updatePaymentMethodDTO.getMethod());
        paymentMethodRepository.save(paymentMethod);
        return paymentMethod;
    }
}
