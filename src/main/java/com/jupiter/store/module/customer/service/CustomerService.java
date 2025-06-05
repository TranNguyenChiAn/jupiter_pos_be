package com.jupiter.store.module.customer.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.customer.dto.CreateCustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        customer.setTotalSpent(0L);
        customer.setTotalOrders(0);
        customer.setCreatedBy(SecurityUtils.getCurrentUserId());
        return customerRepository.save(customer);
    }

    public Customer findCustomer(String keyword) {
        return customerRepository.findByKeyword(keyword)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer findById(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElse(null);
    }

    public Customer findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }


    public Page<Customer> search(Pageable pageable, String search) {
        if (search != null) {
            search = search.trim();
            if (search.isBlank()) {
                search = null;
            } else {
                search = search.toLowerCase();
            }
        }
        return customerRepository.search(search, pageable);
    }

    public void updateAfterOrder(Integer customerId, Long totalSpent, Integer totalOrders) {
        Customer customer = findById(customerId);
        if (customer != null) {
            customer.setTotalSpent(customer.getTotalSpent() + totalSpent);
            customer.setTotalOrders(customer.getTotalOrders() + totalOrders);
            customerRepository.save(customer);
        }
    }
}

