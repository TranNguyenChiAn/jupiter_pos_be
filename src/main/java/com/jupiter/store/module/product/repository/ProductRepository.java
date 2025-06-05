package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(
            value = "SELECT * FROM ( " +
                    "  SELECT DISTINCT p.*, " +
                    "  CASE WHEN (p.product_name ILIKE CONCAT('%', :search, '%') " +
                    "         OR p.description ILIKE CONCAT('%', :search, '%') " +
                    "         OR pv.sku ILIKE CONCAT('%', :search, '%') " +
                    "         OR pv.barcode ILIKE CONCAT('%', :search, '%')) " +
                    "       THEN 0 ELSE 1 END AS ord_expr " +
                    "  FROM products p " +
                    "  LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                    "  LEFT JOIN product_categories pc ON p.id = pc.product_id " +
                    "  WHERE (:status IS NULL OR p.status = :status) " +
                    "    AND (:categoryId IS NULL OR pc.category_id = :categoryId) " +
                    "    AND ( :search IS NULL OR " +
                    "         ((p.product_name ILIKE CONCAT('%', :search, '%') " +
                    "           OR p.description ILIKE CONCAT('%', :search, '%') " +
                    "           OR pv.sku ILIKE CONCAT('%', :search, '%') " +
                    "           OR pv.barcode ILIKE CONCAT('%', :search, '%')) " +
                    "        OR (p.product_name ILIKE ANY(:searchTerms) " +
                    "           OR p.description ILIKE ANY(:searchTerms) " +
                    "           OR pv.sku ILIKE ANY(:searchTerms) " +
                    "           OR pv.barcode ILIKE ANY(:searchTerms))) " +
                    "    ) " +
                    ") sub " +
                    "ORDER BY sub.ord_expr, sub.last_modified_date DESC",
            countQuery = "SELECT COUNT(*) FROM ( " +
                    "  SELECT DISTINCT p.id " +
                    "  FROM products p " +
                    "  LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                    "  LEFT JOIN product_categories pc ON p.id = pc.product_id " +
                    "  WHERE (:status IS NULL OR p.status = :status) " +
                    "    AND (:categoryId IS NULL OR pc.category_id = :categoryId) " +
                    "    AND ( :search IS NULL OR " +
                    "         ((p.product_name ILIKE CONCAT('%', :search, '%') " +
                    "           OR p.description ILIKE CONCAT('%', :search, '%') " +
                    "           OR pv.sku ILIKE CONCAT('%', :search, '%') " +
                    "           OR pv.barcode ILIKE CONCAT('%', :search, '%')) " +
                    "        OR (p.product_name ILIKE ANY(:searchTerms) " +
                    "           OR p.description ILIKE ANY(:searchTerms) " +
                    "           OR pv.sku ILIKE ANY(:searchTerms) " +
                    "           OR pv.barcode ILIKE ANY(:searchTerms))) " +
                    "    ) " +
                    ") sub",
            nativeQuery = true)
    Page<Product> searchProduct(
            @Param("search") String search,
            @Param("searchTerms") String[] searchTerms,
            @Param("categoryId") Integer categoryId,
            @Param("status") String status,
            Pageable pageable
    );
}
