package com.jupiter.store.module.user.service;

import com.jupiter.store.common.security.jwt.JwtUtil;
import com.jupiter.store.module.user.dto.JwtResponse;
import com.jupiter.store.module.user.dto.LoginRequest;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) {
        User user = userRepository.findByPhoneOrEmail(loginRequest.getPhone(), loginRequest.getEmail());

        if (!User.isNotNull(user)) {
            return ResponseEntity.status(401).body(new JwtResponse("Invalid credentials"));
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(new JwtResponse("Invalid credentials"));
        }
        if (!User.isActive(user)) {
            return ResponseEntity.status(401).body(new JwtResponse("User is not active"));
        }
        if (!User.hasRole(user)) {
            return ResponseEntity.status(401).body(new JwtResponse("User does not have a role"));
        }
        
        String tokenParam = "";
        if (User.hasPhone(user)) {
            tokenParam = user.getPhone();
        } else if (User.hasEmail(user)) {
            tokenParam = user.getEmail();
        } else {
            return ResponseEntity.status(401).body(new JwtResponse("User does not have a phone number or email"));
        }
        String token = jwtUtil.createToken(tokenParam, user.getId(), user.getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(new JwtResponse(token));
    }
}