package com.jupiter.store.module.customer.resource;

import com.jupiter.store.module.customer.dto.CreateCustomerDTO;
import com.jupiter.store.module.customer.dto.CustomerSearchDTO;
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
    public ResponseEntity<Page<Customer>> findAllCustomer(@RequestBody CustomerSearchDTO request) {
        Page<Customer> result = customerService.search(
                Pageable.ofSize(request.getSize()).withPage(request.getPage()), request.getSearch()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search-detail")
    public Customer findCustomerById(@RequestParam String keyword) {
        return customerService.findCustomer(keyword);
    }

    @PostMapping("")
    public ResponseEntity<Customer> create(@RequestBody CreateCustomerDTO createCustomerDTO) {
        Customer result = customerService.create(createCustomerDTO);
        return ResponseEntity.ok(result);
    }

}
