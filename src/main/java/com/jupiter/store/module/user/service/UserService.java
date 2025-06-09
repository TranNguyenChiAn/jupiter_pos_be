package com.jupiter.store.module.user.service;

import com.jupiter.store.common.utils.SecurityUtils;
import com.jupiter.store.module.notifications.constant.NotificationEntityType;
import com.jupiter.store.module.notifications.model.Notification;
import com.jupiter.store.module.notifications.service.NotificationService;
import com.jupiter.store.module.role.constant.RoleBase;
import com.jupiter.store.module.user.dto.ChangePasswordDTO;
import com.jupiter.store.module.user.dto.RegisterUserDTO;
import com.jupiter.store.module.user.dto.UpdateUserDTO;
import com.jupiter.store.module.user.dto.UserReadDTO;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserReadDTO> searchUsers(String search, Pageable pageable) {
        Page<User> usersPage;
        if (search != null) {
            search = search.trim();
            if (search.isBlank()) {
                search = null;
            } else {
                search = search.toLowerCase();
            }
        }
        usersPage = userRepository.search(search, pageable);
        return usersPage.map(UserReadDTO::new);
    }

    public UserReadDTO register(RegisterUserDTO registerUserDTO) {
        User existedUser = findByUsernameOrPhoneOrEmail(registerUserDTO.getUsername(), registerUserDTO.getPhone(), registerUserDTO.getEmail());
        if (existedUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại hoặc email đã tồn tại!");
        }

        User existedUserName = searchByUsername(registerUserDTO.getUsername());
        if (existedUserName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username này đã tồn tại!");
        }

        User user = new User();
        String pwd = registerUserDTO.getPassword();
        if (pwd == null || pwd.isBlank()) {
            return null;
        }
        String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setUsername(registerUserDTO.getUsername());
        user.setFullName(registerUserDTO.getFullname());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(registerUserDTO.getPhone());
        RoleBase userRole = registerUserDTO.getRole();
        if (userRole != null) {
            user.setRole(userRole.toString());
        } else {
            user.setRole(RoleBase.EMPLOYEE);
        }
        user.setGender(registerUserDTO.isGender());
        user.setActive(registerUserDTO.isActive());
        user.setCreatedBy(SecurityUtils.getCurrentUserId());
        user.setLastModifiedBy(SecurityUtils.getCurrentUserId());
        return new UserReadDTO(userRepository.save(user));
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

    public User findByUsernameOrPhoneOrEmail(String username, String phone, String email) {
        return userRepository.findByUsernameOrPhoneOrEmail(username, phone, email);
    }

    public User findByUsernameAndPhoneAndEmail(String username, String phone, String email) {
        return userRepository.findByUsernameAndPhoneAndEmail(username, phone, email);
    }

    @Transactional
    public void update(Integer userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy User có ID: " + userId));

        if (updateUserDTO.getUsername() == null && updateUserDTO.getEmail() == null && updateUserDTO.getPhone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cập nhật thông tin người dùng không hợp lệ");
        }
        User userByUsername = userRepository.findByUsername(updateUserDTO.getUsername());
        if (userByUsername != null && !userByUsername.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username này đã tồn tại!");
        }

        User userByEmail = userRepository.findByEmail(updateUserDTO.getEmail());
        if (userByEmail != null && !userByEmail.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email này đã tồn tại!");
        }

        // Check for phone uniqueness
        User userByPhone = userRepository.findByPhone(updateUserDTO.getPhone());
        if (userByPhone != null && !userByPhone.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại này đã tồn tại!");
        }

        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone());
        user.setFullName(updateUserDTO.getFullname());
        user.setGender(updateUserDTO.isGender());
        user.setActive(updateUserDTO.isActive());
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
            user = findByUsernameOrPhoneOrEmail(username, phoneNumber, email);
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với thông tin đã cung cấp");
        }
        return new UserReadDTO(user);
    }

    public void generateOTP(String loginInfo) {
        User currentUser = userRepository.findAccount(loginInfo);
        if (!currentUser.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại");
        }
        Integer otpCode = new Random().nextInt(999999);
        Notification notification = new Notification();
        notification.setUserId(currentUser.getId());
        notification.setTitle("Yêu cầu đặt lại mật khẩu");
        notification.setBody("Mã OTP của bạn là: " + otpCode);
        notification.setEntityType(NotificationEntityType.PASSWORD_RESET);
        notification.setEntityId(otpCode);
        notification.setRead(false);

        notificationService.sendEmail(currentUser.getEmail(), notification);
    }

    public void verifyOtpAndChangePassword(ChangePasswordDTO changePasswordDTO){
        Integer otp = changePasswordDTO.getOtp();
        try {
            Notification notification = notificationService.verifyOtp(otp);
            User currentUser = userRepository.findById(notification.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));
            currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(currentUser);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP không hợp lệ", e);
        }
    }

}