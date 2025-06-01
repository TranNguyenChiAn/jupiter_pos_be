package com.jupiter.store.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsRequestDTO implements Serializable {
    private String search;
    private String sort;
    private String filter; // optional filtering criteria
    private int pageNumber;
    private int pageSize;
}
