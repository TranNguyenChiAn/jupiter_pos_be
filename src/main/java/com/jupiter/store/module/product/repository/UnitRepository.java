package com.jupiter.store.module.product.repository;

import com.jupiter.store.module.product.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM units u " +
            "WHERE LOWER(u.unit_name) = LOWER(:unitName)", nativeQuery = true)
    boolean existsByUnitName(@Param("unitName") String unitName);

    @Query(value = "SELECT * FROM units u WHERE LOWER(u.unit_name) like LOWER(CONCAT('%', :unitName, '%'))", nativeQuery = true)
    List<Unit> findByUnitName(@Param("unitName") String unitName);
}
