package com.pedasco.apex.domain.entity;

import com.pedasco.apex.domain.enums.UpdateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "client_version_history")
public class ClientVersionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "version_number", nullable = false, length = 20)
    private String versionNumber;

    @Column(name = "previous_version", length = 20)
    private String previousVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private UpdateStatus status;

    @CreationTimestamp
    @Column(name = "started_at", updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}