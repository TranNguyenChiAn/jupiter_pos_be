package com.jupiter.store.dto;

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

    private static final long serialVersionUID = 1L;
    @JMap
    private Long id;
    @JMap
    private Long createdBy;
    @JMap
    private LocalDateTime createdAt;
    @JMap
    private Long lastModifiedBy;
    @JMap
    private LocalDateTime lastModifiedAt;
}
