package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.InstallKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstallKeyRepository extends JpaRepository<InstallKey, UUID> {
    Optional<InstallKey> findByKeyAndUsedFalse(String key);
}