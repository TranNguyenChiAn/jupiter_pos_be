package com.jupiter.store.domain;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;


    @Column(name = "username")
    private String username;


    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;


    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "avatar")
    private String avatar;


    @Column(name = "role")
    private String role;

    @Column(name = "is_active")
    private boolean isActive;
}
