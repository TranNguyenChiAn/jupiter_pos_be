package com.jupiter.store.common.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class SetVietnamTimezone {
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }
}
