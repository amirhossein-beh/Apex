package com.pedasco.apex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeploymentResponse {
    private UUID id;
    private String versionNumber;
    private String deploymentType;
    private String status;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<DeploymentClientResponse> deploymentClients;
}
