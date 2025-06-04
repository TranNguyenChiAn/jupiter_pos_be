package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchDTO {
    private Integer pageSize;
    private Integer pageNumber;
}
