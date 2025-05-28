package com.jupiter.store.module.customer.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.dto.CreateCustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(CreateCustomerDTO createCustomerDTO) {
        Customer customer = new Customer();
        customer.setCustomerName(createCustomerDTO.getFullName());
        customer.setGender(createCustomerDTO.isGender());
        customer.setPhone(createCustomerDTO.getPhoneNumber());
        customer.setAddress(createCustomerDTO.getAddress());
        customer.setActive(true);
        customer.setTotalSpent(0L);
        customer.setCreatedBy(SecurityUtils.getCurrentUserId());
        return customerRepository.save(customer);
    }

    public Customer findCustomer(String keyword) {
        return customerRepository.findByKeyword(keyword)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}

