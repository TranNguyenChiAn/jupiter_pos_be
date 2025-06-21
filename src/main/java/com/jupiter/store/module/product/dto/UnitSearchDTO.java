package com.jupiter.store.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitSearchDTO {
    private Integer page;
    private Integer size;
    private String sortBy = "id";
    private String sortDirection = "DESC";
}
