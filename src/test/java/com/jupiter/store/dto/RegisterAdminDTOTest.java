package com.jupiter.store.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterAdminDTOTest {

    @Test
    void getUsername() {
        RegisterAdminDTO registerAdminDTO = new RegisterAdminDTO();
        registerAdminDTO.setUsername("admin");
        assertEquals("admin", registerAdminDTO.getUsername());
    }
}