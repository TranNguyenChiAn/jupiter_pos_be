package com.jupiter.store.service;

import com.jupiter.store.repository.UserRepository;
import com.jupiter.store.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextService {
    @Autowired
    UserRepository userRepository;

    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (authentication.isAuthenticated()) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Object principal = auth.getPrincipal();
                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails customUserDetails = (CustomUserDetails) principal;
                    return customUserDetails.getUserId();
                } else {
                    return null;
                }
            }
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            AnonymousAuthenticationToken anonymousAuthenticationToken = (AnonymousAuthenticationToken) authentication;
            return null;
        }
        return null;
    }
}
