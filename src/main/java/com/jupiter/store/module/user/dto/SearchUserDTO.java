package com.jupiter.store.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
