package com.jupiter.store.module.order.model;


import com.jupiter.store.common.model.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator", strategy = "com.jupiter.store.common.utils.MyGenerator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "product_variant_id")
    private Integer productVariantId;

    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @Column(name = "sold_price")
    private Long soldPrice;

    @Column(name = "total_amount")
    private Long totalAmount;
}
