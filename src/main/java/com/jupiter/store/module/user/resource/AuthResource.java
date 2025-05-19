package com.jupiter.store.module.user.resource;

import com.jupiter.store.module.user.dto.JwtResponse;
import com.jupiter.store.module.user.dto.LoginRequest;
import com.jupiter.store.module.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthResource {
    private final AuthService authService;

    public AuthResource( AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}