package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.InstallKey;
import com.pedasco.apex.repository.InstallKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstallKeyService {

    private final InstallKeyRepository installKeyRepository;

    public List<InstallKey> getAll() {
        return installKeyRepository.findAll();
    }

    public List<InstallKey> getByClientId(UUID clientId) {
        return installKeyRepository.findAll().stream()
                .filter(k -> k.getClient() != null &&
                        k.getClient().getId().equals(clientId))
                .toList();
    }

    public void delete(UUID keyId) {
        InstallKey key = installKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));
        if (key.isUsed())
            throw new RuntimeException("Cannot delete a used key");
        installKeyRepository.delete(key);
    }
}
