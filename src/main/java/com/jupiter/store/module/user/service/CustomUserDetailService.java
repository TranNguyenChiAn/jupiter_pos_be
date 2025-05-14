package com.jupiter.store.module.user.service;

import com.jupiter.store.common.security.CustomUserDetails;
import com.jupiter.store.module.user.model.User;
import com.jupiter.store.module.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetails(
                user.getId(),            // Truyền userId
                user.getPhone(),          // Truyền phone
                user.getPassword(),          // Truyền password
                authorities            // Truyền quyền (authorities)
        );
    }
}
