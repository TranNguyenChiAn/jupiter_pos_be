package com.jupiter.store.module.order.model;

import com.jupiter.store.module.product.model.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderDetailId.class)
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(name = "price")
    private Long price;

    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @Column(name = "sold_price")
    private Long soldPrice;
}
