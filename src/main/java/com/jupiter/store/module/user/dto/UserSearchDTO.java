package com.jupiter.store.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDTO {
    private Integer page = 0;
    private Integer size;
    private String search;
}