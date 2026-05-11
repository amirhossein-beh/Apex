package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.CommandLog;
import com.pedasco.apex.domain.enums.CommandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommandLogRepository extends JpaRepository<CommandLog, UUID> {
    List<CommandLog> findByClientIdOrderBySentAtDesc(UUID clientId);
    List<CommandLog> findByStatus(CommandStatus status);
}