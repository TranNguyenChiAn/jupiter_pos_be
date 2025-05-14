package com.jupiter.store.module.user.resource;

import com.jupiter.store.common.security.jwt.JwtUtil;
import com.jupiter.store.module.user.dto.JwtResponse;
import com.jupiter.store.module.user.dto.LoginRequest;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthResource {
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    public AuthResource(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null) {
            if (user.isActive()) {
                if (user.getRole() != null) {
                    String token = jwtUtil.createToken(user.getPhone(), user.getId(), user.getRole());

                    return ResponseEntity.ok()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .body(new JwtResponse(token));
                } else
                    return ResponseEntity.status(401).body(new JwtResponse("User does not have a role"));

            } else
                return ResponseEntity.status(401).body(new JwtResponse("User is not active"));

        }
        return ResponseEntity.status(401).body(new JwtResponse("Invalid credentials"));
    }
}