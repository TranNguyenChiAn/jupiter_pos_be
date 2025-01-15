package com.jupiter.store.service;

import com.jupiter.store.domain.Customer;
import com.jupiter.store.dto.RegisterCustomer;
import com.jupiter.store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository , PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterCustomer registerCustomer) {
        Customer customer = new Customer();
        String encodedPassword = passwordEncoder.encode(registerCustomer.getPassword());

        customer.setUserName(registerCustomer.getUsername());
        customer.setFirstname(registerCustomer.getFirstname());
        customer.setLastname(registerCustomer.getLastname());
        customer.setEmail(registerCustomer.getEmail());
        customer.setPassword(encodedPassword);
        customer.setPhoneNumber(registerCustomer.getPhone());
        customer.setAddress(registerCustomer.getAddress());
        customer.setCreatedBy(0L);
        customerRepository.save(customer);
    }
}
