package com.jupiter.store.module.customer.dto;

import com.jupiter.store.module.customer.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String customerName;
    private boolean gender = true;
    private String address;
    private String phone;

    public CustomerDTO(Customer customer) {
        this.customerName = customer.getCustomerName();
        this.gender = customer.isGender();
        this.address = customer.getAddress();
        this.phone = customer.getPhone();
    }
}
