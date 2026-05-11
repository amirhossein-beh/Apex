package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    Optional<Version> findByActiveTrue();
}