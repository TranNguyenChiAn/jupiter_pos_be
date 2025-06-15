package com.jupiter.store.module.statistic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestTimeDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}