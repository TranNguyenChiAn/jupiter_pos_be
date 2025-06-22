package com.jupiter.store.module.category.repository;

import com.jupiter.store.module.category.dto.IProductCategoryQueryDTO;
import com.jupiter.store.module.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT c.* FROM categories c INNER JOIN product_categories pc ON c.id = pc.category_id " +
            "WHERE pc.product_id = :productId", nativeQuery = true)
    List<Category> findByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM categories c " +
            "WHERE LOWER(c.category_name) = LOWER(:categoryName)", nativeQuery = true)
    boolean existsByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "SELECT * FROM categories c " +
            "WHERE LOWER(c.category_name) like LOWER(CONCAT('%', :categoryName, '%'))", nativeQuery = true)
    List<Category> findByCategoryName(String lowerCase);

    @Query(value = "SELECT c.*, pc.product_id " +
            "FROM categories c INNER JOIN product_categories pc ON c.id = pc.category_id " +
            "WHERE pc.product_id IN :productIds", nativeQuery = true)
    List<Object[]> findByProductIdIn(@Param("productIds") List<Integer> productIds);

    @Query(value = "SELECT * FROM categories c " +
            "  WHERE (:search IS NULL OR unaccent(:search) = '') " +
            "   OR LOWER(unaccent(c.category_name)) LIKE CONCAT('%', unaccent(:search), '%')",
            countQuery = "SELECT count(id) FROM categories c " +
            "  WHERE (:search IS NULL OR unaccent(:search) = '') " +
            "   OR LOWER(unaccent(c.category_name)) LIKE CONCAT('%', unaccent(:search), '%')",
            nativeQuery = true)
    Page<Category> search(@Param("search") String search, Pageable pageable);
}