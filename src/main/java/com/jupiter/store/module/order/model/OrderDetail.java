package com.jupiter.store.module.order.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "product_variant_id")
    private Integer productVariantId;

    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @Column(name = "sold_price")
    private Long soldPrice;
}
