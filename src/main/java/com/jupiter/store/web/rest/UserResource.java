package com.jupiter.store.web.rest;

import com.jupiter.store.dto.RegisterUserDTO;
import com.jupiter.store.model.User;
import com.jupiter.store.service.UserService;
import com.jupiter.store.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    @Autowired
    private UserService userService;
    @GetMapping("/search/{username}")
    public User searchById(@RequestParam String username) {
        return userService.searchByUsername(username);
    }
    @PostMapping("/register")
    public void register(@RequestBody RegisterUserDTO registerUserDTO) {
        userService.register(registerUserDTO);
    }

    @GetMapping("/current-user-id")
    public Long getUser() {
        return SecurityUtils.getCurrentUserId();
    }
}
//
//    @PutMapping("/update")
//    public void update(@RequestParam Long userId, @RequestBody UpdateUserDTO updateUserDTO) {
//        userService.update(userId, updateUserDTO);
//    }
//
//    @PutMapping("change-password")
//    public void changePassword(@RequestParam Long userId, @RequestBody ChangePasswordDTO changePasswordDTO) {
//        userService.changePassword(userId, changePasswordDTO);
//    }
