package com.jupiter.store.module.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
