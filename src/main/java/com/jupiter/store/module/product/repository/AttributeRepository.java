package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
