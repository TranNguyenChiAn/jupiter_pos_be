package com.jupiter.store.module.order.constant;

import java.util.List;

public enum OrderStatus {
    DON_NHAP,
    CHO_XAC_NHAN,
    DA_XAC_NHAN,
    DANG_VAN_CHUYEN,
    DA_GIAO,
    DA_HUY;

    public static List<String> getAllStatuses() {
        return List.of(
            DON_NHAP.name(),
            CHO_XAC_NHAN.name(),
            DA_XAC_NHAN.name(),
            DANG_VAN_CHUYEN.name(),
            DA_GIAO.name(),
            DA_HUY.name()
        );
    }
}
