package com.jupiter.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "prod-generator")
    @GenericGenerator(name = "prod-generator",
            strategy = "com.jupiter.store.utils.MyGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

}
