package com.jupiter.store.module.user.dto;

import com.jupiter.store.module.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO {
    private Integer id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private boolean isActive;
    private boolean gender;
    private String role;

    public UserReadDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.isActive = user.isActive();
        this.gender = user.isGender();
        this.role = user.getRole();
    }
}
