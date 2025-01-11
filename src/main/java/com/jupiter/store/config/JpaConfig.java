package com.jupiter.store.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import jakarta.persistence.EntityManagerFactory;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.HandlerMethod;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.jupiter.store.repository")
@EnableTransactionManagement
public class JpaConfig {
//    @Bean
//    public OperationCustomizer customize() {
//        return (Operation operation, HandlerMethod handlerMethod) -> {
//            Parameter parameter = new Parameter();
//            parameter.setName("Authorization");
//            parameter.setIn(ParameterIn.HEADER.toString());
//            parameter.setSchema(new StringSchema());
//            operation.addParametersItem(parameter);
//            return operation;
//        };
//    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }
}

