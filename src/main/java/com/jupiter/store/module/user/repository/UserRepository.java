package com.jupiter.store.module.user.repository;

import com.jupiter.store.module.customer.model.Customer;
import com.jupiter.store.module.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users u WHERE u.username = :username", nativeQuery = true)
    User findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users u WHERE u.phone = :phone", nativeQuery = true)
    User findByPhone(@Param("phone") String phone);

    @Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.phone = :phone OR u.email = :email OR u.username = :username", nativeQuery = true)
    User findByPhoneOrEmail(@Param("username") String username, @Param("phone") String phone, @Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.phone = :account OR u.email = :account OR u.username = :account", nativeQuery = true)
    User findAccount(@Param("account") String account);

    @Query(
            value = "SELECT * FROM ( " +
                    "  SELECT u.*, " +
                    "         ts_rank_cd(u.fts, plainto_tsquery('simple', unaccent(:search))) AS rank " +
                    "  FROM users u " +
                    "  WHERE (:search IS NULL OR unaccent(:search) = '' " +
                    "         OR u.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "         OR u.phone LIKE CONCAT('%', :search, '%') " +
                    "         OR u.username LIKE CONCAT('%', :search, '%') " +
                    "         OR u.email LIKE CONCAT('%', :search, '%') " +
                    "        ) " +
                    ") sub " +
                    "ORDER BY " +
                    "  CASE " +
                    "    WHEN (:search IS NULL OR unaccent(:search) = '') THEN NULL " +
                    "    ELSE COALESCE(sub.rank, 0) " +
                    "  END DESC, " +
                    "  sub.last_modified_date DESC nulls last",
            countQuery = "SELECT COUNT(*) FROM users u " +
                    "WHERE (:search IS NULL OR unaccent(:search) = '' " +
                    "       OR u.fts @@ plainto_tsquery('simple', unaccent(:search)) " +
                    "         OR u.phone LIKE CONCAT('%', :search, '%') " +
                    "         OR u.username LIKE CONCAT('%', :search, '%') " +
                    "         OR u.email LIKE CONCAT('%', :search, '%') " +
                    "      )",
            nativeQuery = true)
    Page<User> search(@Param("search") String search, Pageable pageable);
}