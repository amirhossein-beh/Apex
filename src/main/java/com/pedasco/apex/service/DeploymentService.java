package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.DeploymentClient;
import com.pedasco.apex.domain.entity.Version;
import com.pedasco.apex.domain.entity.VersionDeployment;
import com.pedasco.apex.domain.enums.CommandAction;
import com.pedasco.apex.domain.enums.DeploymentClientStatus;
import com.pedasco.apex.domain.enums.DeploymentStatus;
import com.pedasco.apex.domain.enums.DeploymentType;
import com.pedasco.apex.dto.response.DeploymentClientResponse;
import com.pedasco.apex.dto.response.DeploymentResponse;
import com.pedasco.apex.repository.*;
import com.pedasco.apex.websocket.ClientSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final VersionRepository versionRepository;
    private final ClientRepository clientRepository;
    private final VersionDeploymentRepository versionDeploymentRepository;
    private final DeploymentClientRepository deploymentClientRepository;
    private final CommandService commandService;
    private final ClientSessionRegistry clientSessionRegistry;

    public VersionDeployment createPilotDeployment(Long versionId, List<UUID> clientIds, String createdBy) {
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("version not found: " + versionId));

        VersionDeployment deployment = new VersionDeployment();
        deployment.setVersion(version);
        deployment.setDeploymentType(DeploymentType.PILOT);
        deployment.setStatus(DeploymentStatus.PENDING);
        deployment.setCreatedBy(createdBy);
        versionDeploymentRepository.save(deployment);

        for (UUID clientId : clientIds) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("client not found: " + clientId));

            DeploymentClient deploymentClient = new DeploymentClient();
            deploymentClient.setDeployment(deployment);
            deploymentClient.setClient(client);
            deploymentClient.setStatus(DeploymentClientStatus.PENDING);
            deploymentClientRepository.save(deploymentClient);

            if (clientSessionRegistry.isOnline(clientId.toString())) {
                try {
                    commandService.sendCommand(clientId, CommandAction.CHECK_UPDATE, null);
                    deploymentClient.setStatus(DeploymentClientStatus.DOWNLOADING);
                    deploymentClientRepository.save(deploymentClient);
                } catch (Exception e) {
                    log.warn("Failed to send command to: {}-{}", client.getId(), client.getName());
                }
            }
        }

        return deployment;
    }

    public VersionDeployment createFullDeployment(Long versionId, String createdBy) {
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("version not found: " + versionId));

        VersionDeployment deployment = new VersionDeployment();
        deployment.setVersion(version);
        deployment.setDeploymentType(DeploymentType.FULL);
        deployment.setStatus(DeploymentStatus.PENDING);
        deployment.setCreatedBy(createdBy);
        versionDeploymentRepository.save(deployment);

        for (Client client : clientRepository.findAll()) {
            DeploymentClient deploymentClient = new DeploymentClient();
            deploymentClient.setDeployment(deployment);
            deploymentClient.setClient(client);
            deploymentClient.setStatus(DeploymentClientStatus.PENDING);
            deploymentClientRepository.save(deploymentClient);

            if (clientSessionRegistry.isOnline(client.getId().toString())) {
                try {
                    commandService.sendCommand(client.getId(), CommandAction.CHECK_UPDATE, null);
                    deploymentClient.setStatus(DeploymentClientStatus.DOWNLOADING);
                    deploymentClientRepository.save(deploymentClient);
                } catch (Exception e) {
                    log.warn("Failed to send command to: {}", client.getName());
                }
            }
        }

        return deployment;
    }

    public List<DeploymentResponse> getAll() {
        return versionDeploymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DeploymentResponse getDeployment(UUID deploymentId) {
        return toResponse(versionDeploymentRepository.findById(deploymentId)
                .orElseThrow(() -> new RuntimeException("Deployment not found")));
    }

    public DeploymentResponse toResponse(VersionDeployment deployment) {
        List<DeploymentClientResponse> clients = deployment.getDeploymentClients() == null
                ? List.of()
                : deployment.getDeploymentClients()
                .stream()
                .map(dc -> new DeploymentClientResponse(
                        dc.getId(),
                        dc.getClient().getId(),
                        dc.getClient().getName(),
                        dc.getStatus().name(),
                        dc.getStartedAt(),
                        dc.getFinishedAt()
                ))
                .toList();

        return new DeploymentResponse(
                deployment.getId(),
                deployment.getVersion().getVersionNumber(),
                deployment.getDeploymentType().name(),
                deployment.getStatus().name(),
                deployment.getNotes(),
                deployment.getCreatedBy(),
                deployment.getCreatedAt(),
                clients
        );
    }
}
