package com.jupiter.store.module.order.constant;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    DON_NHAP(value = "Đơn nháp"),
    CHO_XAC_NHAN(value = "Chờ xác nhận"),
    DA_XAC_NHAN(value = "Đã xác nhận"),
    DANG_VAN_CHUYEN(value = "Đang vận chuyển"),
    DA_GIAO(value = "Đã giao"),
    HOAN_THANH(value = "Hoàn thành"),
    DA_HUY(value = "Đã hủy");

    private static String value = "";

    OrderStatus(String s) {
    }

    public String getValue() {
        return value;
    }

    public static List<String> getAllStatuses() {
        return List.of(
                DON_NHAP.name(),
                CHO_XAC_NHAN.name(),
                DA_XAC_NHAN.name(),
                DANG_VAN_CHUYEN.name(),
                DA_GIAO.name(),
                HOAN_THANH.name(),
                DA_HUY.name()
        );
    }

    public static final Map<OrderStatus, Set<OrderStatus>> validTransitions;

    static {
        validTransitions = Map.of(
                DON_NHAP, Set.of(CHO_XAC_NHAN, HOAN_THANH, DA_HUY),
                CHO_XAC_NHAN, Set.of(DA_XAC_NHAN, HOAN_THANH, DA_HUY),
                DA_XAC_NHAN, Set.of(DANG_VAN_CHUYEN, HOAN_THANH, DA_HUY),
                DANG_VAN_CHUYEN, Set.of(DA_GIAO, HOAN_THANH, DA_HUY),
                DA_GIAO, Set.of(HOAN_THANH),
                HOAN_THANH, Set.of(), // Cannot change state further
                DA_HUY, Set.of()    // Cannot recover
        );
    }
}
