package com.jupiter.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.jupiter.store")
public class JupiterStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JupiterStoreApplication.class, args);

    }

}
