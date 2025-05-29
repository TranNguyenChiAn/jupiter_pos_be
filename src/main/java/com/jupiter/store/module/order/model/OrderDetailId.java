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
    private Integer productVariant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailId that)) return false;
        return Objects.equals(order, that.order) &&
                Objects.equals(productVariant, that.productVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, productVariant);
    }
}
