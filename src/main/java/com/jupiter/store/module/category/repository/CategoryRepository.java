package com.jupiter.store.module.category.repository;

import com.jupiter.store.module.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM categories c INNER JOIN product_categories pc ON c.id = pc.category_id " +
            "WHERE pc.product_id = :productId", nativeQuery = true)
    List<Category> findByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM categories c " +
            "WHERE LOWER(c.category_name) = LOWER(:categoryName)", nativeQuery = true)
    boolean existsByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "SELECT * FROM categories c " +
            "WHERE LOWER(c.category_name) like LOWER(CONCAT('%', :categoryName, '%'))", nativeQuery = true)
    List<Category> findByCategoryName(String lowerCase);
}