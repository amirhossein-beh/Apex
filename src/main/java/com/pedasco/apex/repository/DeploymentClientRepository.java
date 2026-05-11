package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.DeploymentClient;
import com.pedasco.apex.domain.enums.DeploymentClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeploymentClientRepository extends JpaRepository<DeploymentClient, UUID> {
    List<DeploymentClient> findByDeploymentId(UUID deploymentId);
    List<DeploymentClient> findByClientIdAndStatus(UUID clientId, DeploymentClientStatus status);
}