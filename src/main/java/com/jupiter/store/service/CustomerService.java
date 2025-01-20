package com.jupiter.store.service;

import com.jupiter.store.domain.Customer;
import com.jupiter.store.dto.ChangePasswordDTO;
import com.jupiter.store.dto.RegisterCustomerDTO;
import com.jupiter.store.dto.UpdateCustomerDTO;
import com.jupiter.store.repository.CustomerRepository;
import jakarta.transaction.Transactional;
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

    public void register(RegisterCustomerDTO registerCustomerDTO) {
        Customer customer = new Customer();
        String encodedPassword = passwordEncoder.encode(registerCustomerDTO.getPassword());

        customer.setUserName(registerCustomerDTO.getUsername());
        customer.setFirstname(registerCustomerDTO.getFirstname());
        customer.setLastname(registerCustomerDTO.getLastname());
        customer.setEmail(registerCustomerDTO.getEmail());
        customer.setPassword(encodedPassword);
        customer.setPhoneNumber(registerCustomerDTO.getPhone());
        customer.setAddress(registerCustomerDTO.getAddress());
        customer.setActive(true);
        customer.setCreatedBy(0L);
        customerRepository.save(customer);
    }

    @Transactional
    public void update(Long customerId, UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        customer.setUserName(updateCustomerDTO.getUsername());
        customer.setFirstname(updateCustomerDTO.getFirstname());
        customer.setLastname(updateCustomerDTO.getLastname());
        customer.setPhoneNumber(updateCustomerDTO.getPhone());
        customer.setAddress(updateCustomerDTO.getAddress());
        customerRepository.save(customer);
    }

    @Transactional
    public void changePassword(Long customerId, ChangePasswordDTO changePasswordDTO) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        customer.setPassword(encodedPassword);
        customerRepository.save(customer);
    }
}
