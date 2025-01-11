package com.jupiter.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "payment_methods")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethod extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "method")
    private String method;
}
