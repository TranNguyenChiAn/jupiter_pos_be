package com.jupiter.store.module.user.model;

import com.jupiter.store.common.model.AbstractAuditingEntity;
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
    //    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

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

    public static boolean isNotNull(User user) {
        return user != null;
    }

    public static boolean isActive(User user) {
        return isNotNull(user) && user.isActive();
    }

    public static boolean hasRole(User user) {
        return isNotNull(user) && user.getRole() != null;
    }

    public static boolean hasPhone(User user) {
        return isNotNull(user) && user.getPhone() != null;
    }

    public static boolean hasEmail(User user) {
        return isNotNull(user) && user.getEmail() != null;
    }
}