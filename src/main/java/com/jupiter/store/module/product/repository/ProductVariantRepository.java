package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.dto.ProductVariantReadDTO;
import com.jupiter.store.module.product.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    @Query(value = "DELETE FROM product_variants pv WHERE pv.product_id = :productId", nativeQuery = true)
    void deleteByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT pv.id FROM product_variants pv", nativeQuery = true)
    List<Integer> findAllIds();

    @Query(value = "SELECT * FROM product_variants pv WHERE pv.product_id IN :productIds", nativeQuery = true)
    List<ProductVariant> findByProductIdIn(@Param("productIds") List<Integer> productIds);


    @Query(
            value = "SELECT * FROM ( " +
                    "  SELECT DISTINCT pv.*, pv.last_modified_date as pv_last_modified_date, " +
                    "         ts_rank_cd( to_tsvector('simple', " +
                    "               concat_ws(' ', p.product_name, p.description, pv.sku, pv.barcode) ), " +
                    "               plainto_tsquery('simple', unaccent(:search)) ) as rank " +
                    "  FROM product_variants pv " +
                    "  LEFT JOIN products p ON p.id = pv.product_id " +
                    "  WHERE (COALESCE(unaccent(:search), '') = '' " +
                    "    OR p.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "    OR pv.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "        ) " +
                    ") sub " +
                    "ORDER BY sub.rank DESC, sub.pv_last_modified_date DESC",
            countQuery = "SELECT COUNT(*) FROM ( " +
                    "  SELECT DISTINCT pv.*, pv.last_modified_date as pv_last_modified_date, " +
                    "         ts_rank_cd( to_tsvector('simple', " +
                    "               concat_ws(' ', p.product_name, p.description, pv.sku, pv.barcode) ), " +
                    "               plainto_tsquery('simple', unaccent(:search)) ) as rank " +
                    "  FROM product_variants pv " +
                    "  LEFT JOIN products p ON p.id = pv.product_id " +
                    "  WHERE (COALESCE(unaccent(:search), '') = '' " +
                    "    OR p.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "    OR pv.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "        ) " +
                    ") sub ",
            nativeQuery = true)
    Page<ProductVariant> search(@Param("search") String search, Pageable pageable);
}
