package com.jupiter.store.module.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateOtpDTO {
    private String loginInfo; // This can be phone or email
}
