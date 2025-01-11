package com.jupiter.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
public class SecurityConfig {

    private final String[] ADMIN_FUNCTION = {
            "/admins",
            "/categories"
    };


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/signup").permitAll()
                                .requestMatchers(ADMIN_FUNCTION).hasRole("SYSTEM_ADMIN")
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
//                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .build();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationHandler();
//    }
}
