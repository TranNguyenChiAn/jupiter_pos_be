package com.jupiter.store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "product_variant_id")
    private Long productVariantId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "is_selected")
    private boolean isSelected;
}