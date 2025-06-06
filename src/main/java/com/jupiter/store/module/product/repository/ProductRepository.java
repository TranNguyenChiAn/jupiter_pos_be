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
                    "         ts_rank_cd( to_tsvector('simple', " +
                    "               concat_ws(' ', p.product_name, p.description, pv.sku, pv.barcode) ), " +
                    "               plainto_tsquery('simple', unaccent(:search)) ) as rank " +
                    "  FROM products p " +
                    "  LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                    "  LEFT JOIN product_categories pc ON p.id = pc.product_id " +
                    "  WHERE (:status IS NULL OR p.status = :status) " +
                    "    AND (:categoryId IS NULL OR pc.category_id = :categoryId) " +
                    "    AND (COALESCE(unaccent(:search), '') = '' " +
                    "    OR p.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "    OR pv.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "        ) " +
                    ") sub " +
                    "ORDER BY " +
                    "  CASE " +
                    "    WHEN (:search IS NULL OR unaccent(:search) = '') THEN NULL " +
                    "    ELSE COALESCE(sub.rank, 0) " +
                    "  END DESC, " +
                    "  sub.last_modified_date DESC nulls last",
            countQuery = "SELECT COUNT(*) FROM ( " +
                    "  SELECT DISTINCT p.id " +
                    "  FROM products p " +
                    "  LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                    "  LEFT JOIN product_categories pc ON p.id = pc.product_id " +
                    "  WHERE (:status IS NULL OR p.status = :status) " +
                    "    AND (:categoryId IS NULL OR pc.category_id = :categoryId) " +
                    "    AND (COALESCE(unaccent(:search), '') = '' " +
                    "    OR p.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "    OR pv.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "        ) " +
                    ") sub",
            nativeQuery = true)
    Page<Product> searchProduct(
            @Param("search") String search,
            @Param("categoryId") Integer categoryId,
            @Param("status") String status,
            Pageable pageable
    );
}
