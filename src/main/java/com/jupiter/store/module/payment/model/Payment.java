package com.jupiter.store.module.payment.model;

import com.jupiter.store.common.model.AbstractAuditingEntity;
import com.jupiter.store.module.order.constant.PaymentStatus;
import com.jupiter.store.module.payment.constant.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends AbstractAuditingEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "paid")
    private Long paid;

    @Column(name = "remaining")
    private Long remaining;

    @Column(name = "date")
    private LocalDateTime date;
}