package com.pedasco.apex.domain.entity;

import com.pedasco.apex.domain.enums.DeploymentStatus;
import com.pedasco.apex.domain.enums.DeploymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "version_deployments")
public class VersionDeployment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @Enumerated(EnumType.STRING)
    @Column(name = "deployment_type", nullable = false, length = 10)
    private DeploymentType deploymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private DeploymentStatus status = DeploymentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "deployment", cascade = CascadeType.ALL)
    private List<DeploymentClient> deploymentClients;
}