package com.jupiter.store.module.user.service;

import com.jupiter.store.common.security.jwt.JwtUtil;
import com.jupiter.store.module.user.dto.JwtResponse;
import com.jupiter.store.module.user.dto.LoginRequest;
import com.jupiter.store.module.user.dto.UserReadDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) {
        User user = userRepository.findAccount(loginRequest.getAccount());

        if (user == null) {
            return unauthorized("Thông tin đăng nhập không hợp lệ");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return unauthorized("Mật khẩu không chính xác");
        }

        if (!user.isActive()) {
            return unauthorized("Tài khoản chưa được kích hoạt");
        }

        if (!user.hasRole()) {
            return unauthorized("Tài khoản chưa được cấp quyền truy cập");
        }

        String tokenSubject = extractTokenSubject(user);
        if (tokenSubject == null) {
            return unauthorized("Tài khoản không có số điện thoại hoặc email");
        }

        String token = jwtUtil.createToken(tokenSubject, user.getId(), user.getRole());

        UserReadDTO userReadDTO = userService.searchUserByCriteria(user.getId(), null, null, null, null, null);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(new JwtResponse(token, "Đăng nhập thành công!", userReadDTO));
    }

    private String extractTokenSubject(User user) {
        if (user.hasPhone()) {
            return user.getPhone();
        }
        if (user.hasEmail()) {
            return user.getEmail();
        }
        if (user.hasUsername()) {
            return user.getUsername();
        }
        return null;
    }

    private ResponseEntity<JwtResponse> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse(message));
    }
}