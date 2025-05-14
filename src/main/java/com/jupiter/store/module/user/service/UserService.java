package com.jupiter.store.module.user.service;

import com.jupiter.store.module.role.constant.RoleBase;
import com.jupiter.store.module.user.dto.RegisterUserDTO;
import com.jupiter.store.module.user.dto.UpdateUserDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterUserDTO registerUserDTO) {
        User user = new User();
        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setFullName(registerUserDTO.getFullname());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(registerUserDTO.getPhone());
        user.setRole(RoleBase.USER);
        user.setGender(registerUserDTO.isGender());
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void update(Integer userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setFullName(updateUserDTO.getFullname());
        user.setPhone(updateUserDTO.getPhone());
        user.setGender(updateUserDTO.isGender() );
        userRepository.save(user);
    }

    public User searchByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}