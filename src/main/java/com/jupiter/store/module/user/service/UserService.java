package com.jupiter.store.module.user.service;

import com.jupiter.store.module.role.constant.RoleBase;
import com.jupiter.store.module.user.dto.RegisterUserDTO;
import com.jupiter.store.module.user.dto.UpdateUserDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        User existedUser = findByPhoneOrEmail(registerUserDTO.getPhone(), registerUserDTO.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại!");
        }
        User user = new User();
        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setFullName(registerUserDTO.getFullname());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(registerUserDTO.getPhone());
        user.setRole(RoleBase.USER);
        user.setGender(registerUserDTO.isGender());
        user.setActive(true);
        user.setCreatedBy(0);
        userRepository.save(user);
    }

    public User findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByPhoneOrEmail(String phone, String email) {
        return userRepository.findByPhoneOrEmail(phone, email);
    }

    @Transactional
    public void update(Integer userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found with id: " + userId));
        User existedUser = findByPhoneOrEmail(updateUserDTO.getPhone(), updateUserDTO.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại!");
        }
        user.setFullName(updateUserDTO.getFullname() == null ? user.getFullName() : updateUserDTO.getFullname());
        user.setPhone(updateUserDTO.getPhone() == null ? user.getPhone() : updateUserDTO.getPhone());
        user.setGender(updateUserDTO.isGender() == updateUserDTO.isGender() ? user.isGender() : updateUserDTO.isGender());
        userRepository.save(user);
    }

    public User searchByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}