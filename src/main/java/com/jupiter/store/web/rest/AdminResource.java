package com.jupiter.store.web.rest;

import com.jupiter.store.domain.Admin;
import com.jupiter.store.dto.AdminDTO;
import com.jupiter.store.dto.RegisterAdminDTO;
import com.jupiter.store.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminResource {
    @Autowired
    private AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get all admins")
    @GetMapping("/search")
    public ResponseEntity<List<Admin>> search() {
       return adminService.search();
    }

    @Operation(summary = "Get admin by id")
    @GetMapping("/search/{id}")
    public ResponseEntity<Admin> searchById(Long id) {
        return adminService.searchById(id);
    }

    @Operation(summary = "Register admin")
    @PostMapping("/register")
    public void addAdmin(@RequestBody RegisterAdminDTO adminDTO) {
        adminService.registerAdmin(adminDTO);
    }

    @Operation(summary = "Delete admin")
    @DeleteMapping("/delete")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }

    @Operation(summary = "Update admin")
    @PutMapping("/update")
    public void updateAdmin(@RequestBody AdminDTO adminDTO) {
        adminService.updateAdmin(adminDTO);
    }
}
