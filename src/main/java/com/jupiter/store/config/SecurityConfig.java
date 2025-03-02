package com.jupiter.store.config;

import com.jupiter.store.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/swagger-ui.html", "/api/auth/**",
            "/api/test/**", "/authenticate", "api/users/register" };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/customers/register").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(Customizer.withDefaults())
                .build();
    }

//    @Bean
//// authentication
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails admin = User.withUsername("admin")
//                .password(encoder.encode("admin"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("user")
//                .password(encoder.encode("user"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
