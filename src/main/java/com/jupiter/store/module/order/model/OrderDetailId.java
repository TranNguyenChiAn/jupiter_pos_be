package com.jupiter.store.module.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailId implements Serializable {
    private Integer order;
    private Integer productVariantId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailId)) return false;
        OrderDetailId that = (OrderDetailId) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(productVariantId, that.productVariantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, productVariantId);
    }
}
