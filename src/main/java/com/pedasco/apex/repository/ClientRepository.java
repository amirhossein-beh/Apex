package com.pedasco.apex.repository;

import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    List<Client> findByStatus(ClientStatus status);
    Optional<Client> findByToken(String token);
}