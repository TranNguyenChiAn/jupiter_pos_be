package com.jupiter.store.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class VietQRConfig {
    @Value("${vietqr.account-name}")
    private String vietQrAccountName;

    @Value("${vietqr.bank-code}")
    private String vietQrBankCode;

    @Value("${vietqr.bank-account}")
    private String vietQrBankAccount;

    @Value("${vietqr.user-bank-name}")
    private String vietQrUserBankName;
}

