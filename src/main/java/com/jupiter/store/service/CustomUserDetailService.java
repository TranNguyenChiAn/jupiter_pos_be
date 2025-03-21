package com.jupiter.store.service;

import com.jupiter.store.model.User;
import com.jupiter.store.repository.UserRepository;
import com.jupiter.store.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

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
                user.getUsername(),          // Truyền username
                user.getPassword(),          // Truyền password
                authorities            // Truyền quyền (authorities)
        );
    }
}
