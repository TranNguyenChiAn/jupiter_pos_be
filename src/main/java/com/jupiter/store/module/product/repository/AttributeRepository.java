package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<ProductAttribute, Integer> {
}
