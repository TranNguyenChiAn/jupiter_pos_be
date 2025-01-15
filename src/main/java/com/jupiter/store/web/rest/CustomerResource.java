package com.jupiter.store.web.rest;

import com.jupiter.store.dto.RegisterCustomer;
import com.jupiter.store.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterCustomer registerCustomer) {
        customerService.register(registerCustomer);
    }
}
