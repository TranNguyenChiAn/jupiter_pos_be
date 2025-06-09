package com.jupiter.store.module.user.dto;

import com.jupiter.store.module.role.constant.RoleBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String username;
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private boolean gender;
    private RoleBase role;
    private boolean active;
}
