package com.jupiter.store.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void getId() {
        Admin admin = new Admin();
        admin.setActive(true);
        assertEquals(true, admin.isActive());
    }

    @Test
    void getUsername() {
    }

    @Test
    void getEmail() {
    }

    @Test
    void getPassword() {
    }
}