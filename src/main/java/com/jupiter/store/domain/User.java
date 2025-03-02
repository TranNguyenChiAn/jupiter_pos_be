package com.jupiter.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private  String username;
    @Column(name = "firstname")
    private  String firstname;
    @Column(name = "lastname")
    private  String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "role")
    private String role;
}
