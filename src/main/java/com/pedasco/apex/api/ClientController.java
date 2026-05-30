package com.pedasco.apex.api;

import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.ClientConfig;
import com.pedasco.apex.domain.entity.InstallKey;
import com.pedasco.apex.dto.request.UpdateClientRequest;
import com.pedasco.apex.dto.response.ClientDetailsResponse;
import com.pedasco.apex.dto.response.ClientResponse;
import com.pedasco.apex.dto.response.InstallKeyResponse;
import com.pedasco.apex.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity.ok(clientService.getAllClient());
    }

    @GetMapping("/{id}")
    public ClientDetailsResponse getById(@PathVariable UUID id) {
        return clientService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(
            @PathVariable UUID id,
            @RequestBody UpdateClientRequest request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.deleteById(id));
    }

    @GetMapping("/{id}/configs")
    public ResponseEntity<List<ClientConfig>> getConfigs(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.getConfig(id));
    }

    @PutMapping("/{id}/configs")
    public ResponseEntity<ClientConfig> updateConfig(
            @PathVariable UUID id,
            @RequestParam String configKey,
            @RequestParam String configValue) {
        return ResponseEntity.ok(clientService.updateConfig(id, configKey, configValue));
    }

    @PostMapping("/{id}/install-keys")
    public InstallKeyResponse generateInstallKey(
            @PathVariable UUID id,
            Authentication authentication) {
        return clientService.generateInstallKey(id, authentication.getName());
    }
}