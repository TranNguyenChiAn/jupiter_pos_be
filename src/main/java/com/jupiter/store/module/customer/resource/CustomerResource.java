package com.jupiter.store.module.customer.resource;

import com.jupiter.store.module.customer.dto.CreateCustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerResource {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/search")
    public List<Customer> findAllCustomer() {
        return customerService.findAllCustomer();
    }

    @GetMapping("/search-detail")
    public Customer findCustomerById(@RequestParam String keyword) {
        return customerService.findCustomer(keyword);
    }

    @PostMapping("/add-customer")
    public Customer addCustomer(@RequestBody CreateCustomerDTO createCustomerDTO) {
        return customerService.addCustomer(createCustomerDTO);
    }

}
