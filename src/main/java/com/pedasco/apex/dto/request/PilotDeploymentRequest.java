package com.pedasco.apex.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PilotDeploymentRequest {
    private Long versionId;
    private List<UUID> clientIds;
}
