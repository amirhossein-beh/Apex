package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrafficLogRepository extends JpaRepository<TrafficLog, UUID> {
    List<TrafficLog> findByClientIdOrderByReceivedAtDesc(UUID clientId);
//    List<TrafficLog> findByForwardedToGhaidirFalse();
}