package com.jupiter.store.model;

import com.jupiter.store.constant.PaymentMethod;
import com.jupiter.store.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "date")
    private LocalDateTime date;
}
