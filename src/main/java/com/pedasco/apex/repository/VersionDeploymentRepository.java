package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.VersionDeployment;
import com.pedasco.apex.domain.enums.DeploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VersionDeploymentRepository extends JpaRepository<VersionDeployment, UUID> {
    List<VersionDeployment> findByStatus(DeploymentStatus status);
}