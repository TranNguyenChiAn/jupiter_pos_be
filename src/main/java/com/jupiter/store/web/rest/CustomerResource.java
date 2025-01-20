package com.jupiter.store.web.rest;

import com.jupiter.store.dto.ChangePasswordDTO;
import com.jupiter.store.dto.RegisterCustomerDTO;
import com.jupiter.store.dto.UpdateCustomerDTO;
import com.jupiter.store.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterCustomerDTO registerCustomerDTO) {
        customerService.register(registerCustomerDTO);
    }


    @PutMapping("/update")
    public void update(@RequestParam Long customerId, @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        customerService.update(customerId, updateCustomerDTO);
    }

    @PutMapping("change-password")
    public void changePassword(@RequestParam Long customerId, @RequestBody ChangePasswordDTO changePasswordDTO) {
        customerService.changePassword(customerId, changePasswordDTO);
    }
}
