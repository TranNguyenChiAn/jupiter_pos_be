package com.jupiter.store.module.user.resource;

import com.jupiter.store.common.security.jwt.JwtUtil;
import com.jupiter.store.module.user.dto.JwtResponse;
import com.jupiter.store.module.user.dto.LoginRequest;
import com.jupiter.store.module.user.repository.UserRepository;
import com.jupiter.store.module.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthResource {
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    @Autowired
    private UserRepository userRepository;

    public AuthResource(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}