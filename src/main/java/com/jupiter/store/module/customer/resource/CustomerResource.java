package com.jupiter.store.module.customer.resource;

import com.jupiter.store.module.customer.dto.CreateCustomerDTO;
import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerResource {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/search")
    public ResponseEntity<Page<Customer>> findAllCustomer(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort
    ) {
        Page<Customer> result = customerService.search(
                Pageable.ofSize(size).withPage(page), search
        );
        return ResponseEntity.ok(result);
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
