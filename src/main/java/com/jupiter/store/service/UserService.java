package com.jupiter.store.service;

import com.jupiter.store.constant.RoleBase;
import com.jupiter.store.model.User;
import com.jupiter.store.dto.ChangePasswordDTO;
import com.jupiter.store.dto.RegisterUserDTO;
import com.jupiter.store.dto.UpdateUserDTO;
import com.jupiter.store.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void register(RegisterUserDTO registerUserDTO) {
        User user = new User();
        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setUsername(registerUserDTO.getUsername());
        user.setFirstname(registerUserDTO.getFirstname());
        user.setLastname(registerUserDTO.getLastname());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(registerUserDTO.getPhone());
        user.setAddress(registerUserDTO.getAddress());
        user.setRole(RoleBase.USER);
        user.setActive(true);
        user.setCreatedBy(0L);
        userRepository.save(user);
    }
    @Transactional
    public void update(Long userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setUsername(updateUserDTO.getUsername());
        user.setFirstname(updateUserDTO.getFirstname());
        user.setLastname(updateUserDTO.getLastname());
        user.setPhoneNumber(updateUserDTO.getPhone());
        user.setAddress(updateUserDTO.getAddress());
        userRepository.save(user);
    }
    public User searchByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}