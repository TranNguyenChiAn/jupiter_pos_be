package com.jupiter.store.module.user.model;

import com.jupiter.store.common.model.AbstractAuditingEntity;
import com.jupiter.store.module.role.constant.RoleBase;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "role")
    private String role;

    @Column(name = "gender")
    private boolean gender;

    public boolean hasRole() {
        return this.getRole() != null;
    }

    public boolean hasPhone() {
        return this.getPhone() != null;
    }

    public boolean hasEmail() {
        return this.getEmail() != null;
    }

    public boolean hasUsername() {
        return this.getUsername() != null;
    }

    public boolean isAdmin() {
        return this.getRole().equalsIgnoreCase(RoleBase.ADMIN);
    }

    public boolean canViewOrder() {
        return isAdmin() || this.getRole().equalsIgnoreCase(RoleBase.EMPLOYEE);
    }

    public boolean canUpdateOrder() {
        return isAdmin() || this.getRole().equalsIgnoreCase(RoleBase.EMPLOYEE);
    }

    public boolean canDeleteOrder() {
        return isAdmin();
    }
}