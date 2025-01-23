package com.jupiter.store.repository;

import com.jupiter.store.domain.Category;
import com.jupiter.store.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c.* FROM categories c " +
            "INNER JOIN product_categories pc ON c.id = pc.category_id " +
            "WHERE pc.product_id = :productId", nativeQuery = true)
    List<Category> findByProductId(@Param("productId") Long productId);
}
