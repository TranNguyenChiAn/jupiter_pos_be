package com.jupiter.store.model;

import org.junit.jupiter.api.Test;

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