package com.jupiter.store.module.user.repository;

import com.jupiter.store.module.user.model.User;
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

    @Query(value = "SELECT * FROM users u WHERE u.phone = :phone OR u.email = :email", nativeQuery = true)
    User findByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.phone = :account OR u.email = :account OR u.username = :account", nativeQuery = true)
    User findAccount(@Param("account") String account);
}