package com.jupiter.store.common.security.jwt;

import com.jupiter.store.common.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class AccountStatusFilter extends OncePerRequestFilter {

    public AccountStatusFilter() {
        super();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&  authentication.getPrincipal() instanceof CustomUserDetails springSecurityUser) {
            if (!springSecurityUser.isActive()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Tài khoản của bạn đã bị khóa");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}