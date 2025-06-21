package com.jupiter.store.module.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchDTO {
    private String search;
    private String sortBy = "id";
    private String sortDirection = "DESC";
    private int page = 0;
    private int size = 5;

}
