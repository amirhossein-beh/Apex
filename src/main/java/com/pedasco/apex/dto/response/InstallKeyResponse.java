package com.pedasco.apex.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class InstallKeyResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private String key;
    private ClientResponse client;
    private Boolean used;
    private String createdBy;
    private LocalDateTime usedAt;
    private LocalDateTime expiresAt;
}
