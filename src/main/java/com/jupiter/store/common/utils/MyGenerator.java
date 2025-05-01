package com.jupiter.store.common.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class MyGenerator implements IdentifierGenerator {

    private String prefix;
    private GenIdService genIdService;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return genIdService.nextId();
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) {
        prefix = properties.getProperty("prefix");
        genIdService = new GenIdService();
    }
}
