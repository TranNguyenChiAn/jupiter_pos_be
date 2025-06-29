package com.jupiter.store.common.utils;

import org.springframework.stereotype.Service;

@Service
public class HelperUtils {

    public String convertCamelCaseToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder snakeCase = new StringBuilder();
        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!snakeCase.isEmpty()) {
                    snakeCase.append('_');
                }
                snakeCase.append(Character.toLowerCase(c));
            } else {
                snakeCase.append(c);
            }
        }
        return snakeCase.toString();
    }

    public void validatePageAndSize(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be non-negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
    }

    public String normalizeSearch(String search) {
        if (search != null) {
            search = search.trim();
            if (search.isBlank()) {
                search = null;
            } else {
                search = search.toLowerCase();
            }
        }
        return search;
    }

    public String normalizeSort(String sort) {
        if (sort != null) {
            sort = sort.trim();
            if (sort.isBlank()) {
                sort = null;
            } else {
                sort = convertCamelCaseToSnakeCase(sort);
            }
        }
        return sort;
    }

    public String normalizeSortDirection(String sortDirection) {
        if (sortDirection != null) {
            sortDirection = sortDirection.trim().toLowerCase();
            if (!sortDirection.equals("asc") && !sortDirection.equals("desc")) {
                throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'");
            }
        } else {
            sortDirection = "desc";
        }
        return sortDirection;
    }
}
