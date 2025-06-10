package com.jupiter.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = "com.jupiter.store")
@EnableAsync
@EnableMethodSecurity
public class JupiterStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JupiterStoreApplication.class, args);

    }

}
