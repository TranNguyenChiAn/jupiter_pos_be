package com.jupiter.store.repository;

import com.jupiter.store.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.username = :username", nativeQuery = true)
    User findByUsername(@Param("username") String username);
}
