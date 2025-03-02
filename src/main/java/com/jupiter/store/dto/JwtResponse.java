package com.jupiter.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private  String jwttoken;
}
