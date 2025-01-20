package com.jupiter.store.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private List<Long> productId;
    private String name;
    private double percentage;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean isActive = true;
}
