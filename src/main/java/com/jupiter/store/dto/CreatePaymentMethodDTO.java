package com.jupiter.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodDTO {
    @NotNull
    private String method;
}
