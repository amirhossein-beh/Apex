package com.pedasco.apex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeploymentClientResponse {
    private UUID id;
    private UUID clientId;
    private String clientName;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
