package com.jupiter.store.common.dto;

import com.googlecode.jmapper.annotations.JMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAuditingDTO implements Serializable {
    @JMap
    private Integer id;
    @JMap
    private Integer createdBy;
    @JMap
    private LocalDateTime createdAt;
    @JMap
    private Integer lastModifiedBy;
    @JMap
    private LocalDateTime lastModifiedAt;
}
