package com.pedasco.apex.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "install_keys")
public class InstallKey extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "is_used", nullable = false)
    private boolean used = false;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}