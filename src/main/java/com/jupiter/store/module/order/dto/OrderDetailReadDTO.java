package com.jupiter.store.module.order.dto;

import com.jupiter.store.module.order.model.OrderDetail;
import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailReadDTO {
    private ProductVariantReadDTO productVariant;
    private Long price;
    private Integer soldQuantity;
    private Long soldPrice;

    public OrderDetailReadDTO(OrderDetail orderDetail) {
        this.price = orderDetail.getPrice();
        this.soldQuantity = orderDetail.getSoldQuantity();
        this.soldPrice = orderDetail.getSoldPrice();
    }
}
