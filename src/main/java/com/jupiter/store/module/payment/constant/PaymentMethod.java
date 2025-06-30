package com.jupiter.store.module.payment.constant;

public enum PaymentMethod {
    TIEN_MAT,
    BANKING;

    public static PaymentMethod fromString(String method) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.name().equalsIgnoreCase(method)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("No constant with method " + method + " found");
    }
}