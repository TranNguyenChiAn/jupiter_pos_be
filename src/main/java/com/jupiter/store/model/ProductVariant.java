package com.jupiter.store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "price")
    private int price;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "attribute_id")
    private Long attributeId;
    @Column(name = "attribute_value")
    private String attributeValue;
    @Column(name = "image_path")
    private String imagePath;
}