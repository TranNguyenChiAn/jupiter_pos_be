package com.jupiter.store.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String fullname;
    private String email;
    private String password;
    private String address;
    private String phone;
    private boolean gender;
}
