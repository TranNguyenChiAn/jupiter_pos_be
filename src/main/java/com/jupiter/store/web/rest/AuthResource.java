package com.jupiter.store.web.rest;

import com.jupiter.store.domain.User;
import com.jupiter.store.dto.JwtResponse;
import com.jupiter.store.dto.LoginRequest;
import com.jupiter.store.repository.UserRepository;
import com.jupiter.store.security.jwt.JwtUtil;
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

    @Autowired
    private UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthResource(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null ) {
            if(user.isActive()){
                if (user.getRole() != null){
                    String token = jwtUtil.createToken(loginRequest.getUsername());

                    return ResponseEntity.ok()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .body(new JwtResponse(token));
                }else
                    return ResponseEntity.status(401).body(new JwtResponse("User does not have a role"));

            }else
                return ResponseEntity.status(401).body(new JwtResponse("User is not active"));

        }
        return ResponseEntity.status(401).body(new JwtResponse("Invalid credentials"));
    }
}
