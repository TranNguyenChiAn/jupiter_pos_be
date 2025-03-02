package com.jupiter.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
}
