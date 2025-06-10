package com.jupiter.store.common.utils;

import com.jupiter.store.module.role.constant.RoleBase;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Autowired
    UserRepository userRepository;

    @Bean
    public CommandLineRunner initAdminAccount(UserRepository userRepository,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            User user = userRepository.findByUsername("admin");
            if (user == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setFullName("Administrator");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setEmail("admin@localhost");
                admin.setPhone("0123456789");
                admin.setActive(true);
                admin.setRole(RoleBase.ADMIN);
                admin.setCreatedBy(0);
                userRepository.save(admin);
            }
        };
    }
}
