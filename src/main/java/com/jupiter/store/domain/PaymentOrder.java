package com.jupiter.store.domain;

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
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "date")
    private LocalDateTime date;
}
