package com.jupiter.store.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String message;
    private UserReadDTO user;

    public JwtResponse(String message) {
        this.message = message;
    }
}
