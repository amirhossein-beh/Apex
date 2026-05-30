package com.pedasco.apex.api;

import com.pedasco.apex.domain.entity.InstallKey;
import com.pedasco.apex.service.InstallKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/install-keys")
@RequiredArgsConstructor
public class InstallKeyController {

    private final InstallKeyService installKeyService;

    @GetMapping
    public ResponseEntity<List<InstallKey>> getAll() {
        return ResponseEntity.ok(installKeyService.getAll());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<InstallKey>> getByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(installKeyService.getByClientId(clientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        installKeyService.delete(id);
        return ResponseEntity.ok("Key deleted");
    }
}