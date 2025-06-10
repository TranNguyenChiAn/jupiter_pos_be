package com.jupiter.store.module.user.resource;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.user.dto.*;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.service.UserService;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    @Autowired
    private UserService userService;

    @PostMapping("/search-users")
    public Page<UserReadDTO> search(@RequestBody UserSearchDTO request) {
        return userService.searchUsers(request.getSearch(), Pageable.ofSize(request.getSize()).withPage(request.getPage()));
    }

    @GetMapping("/{userId}")
    public UserReadDTO getUserById(@PathVariable Integer userId) {
        User user = userService.findById(userId);
        return new UserReadDTO(user);
    }

    @PostMapping
    public UserReadDTO createUser(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.register(registerUserDTO);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable Integer userId, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.update(userId, updateUserDTO);
    }

    @GetMapping("/search/{username}")
    public User searchById(@RequestParam String username) {
        return userService.searchByUsername(username);
    }

    @GetMapping("/current-user-id")
    public Integer getUser() {
        return SecurityUtils.getCurrentUserId();
    }

    @PostMapping("/search")
    public UserReadDTO getUserByCriteria(
            @RequestBody SearchUserDTO request
    ) {
        return userService.searchUserByCriteria(
                request.getId(),
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber()
        );
    }

    @PostMapping("/generate-otp")
    public void generateOTP(@RequestBody GenerateOtpDTO generateOtpDTO) {
        userService.generateOTP(generateOtpDTO.getLoginInfo());
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.verifyOtpAndChangePassword(changePasswordDTO);
    }

    @PutMapping("/deactivate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivateUser(@PathVariable Integer userId) {
        userService.deactivateUser(userId);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Integer userId) {
        User user = userService.findById(userId);
        if (user.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa người dùng đang hoạt động!");
        }
        userService.deleteUser(userId);
    }
}


