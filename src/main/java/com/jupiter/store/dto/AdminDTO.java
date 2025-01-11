package com.jupiter.store.dto;

import com.jupiter.store.constant.RoleBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO extends AbstractAuditingDTO{
    private String username;

    private String email;

    private String password;

    private String phoneNumber;

    private String avatar;

    private RoleBase role;

    private boolean isActive;
}
