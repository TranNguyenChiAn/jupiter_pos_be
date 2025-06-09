package com.jupiter.store.module.user.dto;

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
    private String fullname;
    private String email;
    private String phone;
    private boolean gender;
    private boolean active;
}
