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
}
