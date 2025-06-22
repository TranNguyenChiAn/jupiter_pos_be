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

    public String normalizeSearch (String search) {
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
}
