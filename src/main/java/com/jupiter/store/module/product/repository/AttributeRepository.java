package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<ProductAttribute, Integer> {
    @Query(value = "SELECT CASE WHEN COUNT(pa) > 0 THEN true ELSE false END FROM product_attributes pa " +
            "WHERE LOWER(pa.attribute_name) = LOWER(:attributeName)", nativeQuery = true)
    boolean existsByAttributeName(@Param("attributeName") String attributeName);

    @Query(value = "SELECT * FROM product_attributes pa " +
            "WHERE LOWER(pa.attribute_name) like LOWER(CONCAT('%', :attributeName, '%'))", nativeQuery = true)
    List<ProductAttribute> findByAttributeName(@Param("attributeName") String attributeName);
}
