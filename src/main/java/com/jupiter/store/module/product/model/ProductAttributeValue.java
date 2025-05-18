package com.jupiter.store.module.product.model;

import com.jupiter.store.common.model.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_attribute_values")
public class ProductAttributeValue extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_variant_id")
    private Integer productVariantId;

    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "attr_value")
    private String attrValue;

    @Column(name = "unit_id")
    private Integer unitId;
}
