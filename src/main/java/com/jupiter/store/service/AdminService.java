package com.jupiter.store.service;

import com.googlecode.jmapper.JMapper;
import com.jupiter.store.constant.RoleBase;
import com.jupiter.store.domain.Admin;
import com.jupiter.store.dto.AdminDTO;
import com.jupiter.store.dto.RegisterAdminDTO;
import com.jupiter.store.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<List<Admin>> search() {
        return ResponseEntity.ok().body(adminRepository.findAll());
    }

    public ResponseEntity<Admin> searchById(Long id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        return ResponseEntity.ok().body(admin);
    }

    public void registerAdmin(RegisterAdminDTO registerAdminDTO) {
        Admin admin = new Admin();
        admin.setUsername(registerAdminDTO.getUsername());
        admin.setEmail(registerAdminDTO.getEmail());
        admin.setPassword(registerAdminDTO.getPassword());
        admin.setRole(RoleBase.ADMIN);
        admin.setActive(true);
        admin.setCreatedBy(0L);
        admin.setCreatedDate(LocalDateTime.now());
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public void updateAdmin(AdminDTO adminDTO) {
        JMapper<Admin, AdminDTO> mapper = new JMapper<>(Admin.class, AdminDTO.class);
        Admin admin = mapper.getDestination(adminDTO);
        adminRepository.save(admin);
    }
}
