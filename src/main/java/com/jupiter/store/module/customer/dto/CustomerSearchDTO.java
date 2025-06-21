package com.jupiter.store.module.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchDTO {
    private Integer page = 0;
    private Integer size;
    private String search;
    private String sortBy = "lastModifiedDate";
    private String sortDirection = "DESC";
}
