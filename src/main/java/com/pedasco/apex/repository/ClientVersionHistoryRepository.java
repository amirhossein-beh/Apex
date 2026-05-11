package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.ClientVersionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientVersionHistoryRepository extends JpaRepository<ClientVersionHistory, UUID> {
    List<ClientVersionHistory> findByClientIdOrderByStartedAtDesc(UUID clientId);
}