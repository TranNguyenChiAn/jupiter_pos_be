package com.jupiter.store.module.order.constant;

public enum OrderType {
    MUA_TRUC_TIEP("Mua trực tiếp"),
    MUA_ONLINE("Mua online");

    private final String name;

    OrderType(String name) {
        this.name = name;
    }
}
