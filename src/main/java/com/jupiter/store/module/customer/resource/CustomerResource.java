package com.jupiter.store.module.customer.resource;

import com.jupiter.store.module.customer.dto.CustomerDTO;
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

    @GetMapping("/{customerId}")
    public Customer findCustomerById(@PathVariable Integer customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) {
            throw new RuntimeException("Xảy ra lỗi khi tìm kiếm khách hàng");
        }
        return customer;
    }

    @PostMapping("")
    public ResponseEntity<Customer> create(@RequestBody CustomerDTO customerDTO) {
        Customer result = customerService.create(customerDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable Integer customerId, @RequestBody CustomerDTO customerDTO) {
        Customer result = customerService.update(customerId, customerDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{customerId}")
    public void delete(@PathVariable Integer customerId) {
        customerService.deleteUser(customerId);
    }
}
