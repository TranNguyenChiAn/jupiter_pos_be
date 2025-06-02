package com.jupiter.store.module.user.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.role.constant.RoleBase;
import com.jupiter.store.module.user.dto.ChangePasswordDTO;
import com.jupiter.store.module.user.dto.RegisterUserDTO;
import com.jupiter.store.module.user.dto.UpdateUserDTO;
import com.jupiter.store.module.user.dto.UserReadDTO;
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
        User existedUser = findByPhoneOrEmail(registerUserDTO.getUsername(), registerUserDTO.getPhone(), registerUserDTO.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại!");
        }

        User existedUserName = searchByUsername(registerUserDTO.getUsername());
        if (existedUserName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username này đã tồn tại!");
        }

        User user = new User();
        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setUsername(registerUserDTO.getUsername());
        user.setFullName(registerUserDTO.getFullname());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(registerUserDTO.getPhone());
        user.setRole(registerUserDTO.getRole().toString());
        user.setGender(registerUserDTO.isGender());
        user.setActive(true);
        user.setCreatedBy(SecurityUtils.getCurrentUserId());
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

    public User findByPhoneOrEmail(String username, String phone, String email) {
        return userRepository.findByPhoneOrEmail(username, phone, email);
    }

    @Transactional
    public void update(Integer userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with id: " + userId));
        User existedUser = findByPhoneOrEmail(updateUserDTO.getUsername(), updateUserDTO.getPhone(), updateUserDTO.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại!");
        }

        User existedUserName = searchByUsername(updateUserDTO.getUsername());
        if (existedUserName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username này đã tồn tại!");
        }
        user.setUsername(updateUserDTO.getUsername() == null ? user.getUsername() : updateUserDTO.getUsername());
        user.setFullName(updateUserDTO.getFullname() == null ? user.getFullName() : updateUserDTO.getFullname());
        user.setEmail(updateUserDTO.getEmail() == null ? user.getEmail() : updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone() == null ? user.getPhone() : updateUserDTO.getPhone());
        user.setGender(updateUserDTO.isGender() == updateUserDTO.isGender() ? user.isGender() : updateUserDTO.isGender());
        userRepository.save(user);
    }

    public User searchByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserReadDTO searchUserByCriteria(Integer id, String username, String email, String phoneNumber, String firstName, String lastName) {
        User user = null;
        if (id != null && username == null && email == null && phoneNumber == null && firstName == null && lastName == null) {
            user = userRepository.findById(id).orElse(null);
        }
        //todo: find by firstName and lastName...
        if (user == null && (username != null || email != null || phoneNumber != null)) {
            user = findByPhoneOrEmail(username, phoneNumber, email);
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với thông tin đã cung cấp");
        }
        return new UserReadDTO(user);
    }
}