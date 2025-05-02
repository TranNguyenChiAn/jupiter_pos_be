package com.jupiter.store.common.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.generator.Generator;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.EnumSet;

public class MyGenerator implements IdentifierGenerator, Generator {

    private GenIdService genIdService;

    public MyGenerator() {
        this.genIdService = new GenIdService();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return genIdService.nextId();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
