package com.pedasco.apex.api;



import com.pedasco.apex.domain.entity.VersionDeployment;
import com.pedasco.apex.dto.request.FullDeploymentRequest;
import com.pedasco.apex.dto.request.PilotDeploymentRequest;
import com.pedasco.apex.dto.response.DeploymentResponse;
import com.pedasco.apex.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("api/deployments")
@RequiredArgsConstructor
public class DeploymentController {
    private final DeploymentService deploymentService;

    @PostMapping("/pilot")
    public ResponseEntity<DeploymentResponse> pilot(
            @RequestBody PilotDeploymentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(deploymentService.toResponse(
                deploymentService.createPilotDeployment(
                        request.getVersionId(),
                        request.getClientIds(),
                        authentication.getName())));
    }

    @PostMapping("/full")
    public ResponseEntity<DeploymentResponse> full(
            @RequestBody FullDeploymentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(deploymentService.toResponse(
                deploymentService.createFullDeployment(
                        request.getVersionId(),
                        authentication.getName())));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeploymentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(deploymentService.getDeployment(id));
    }

    @GetMapping
    public ResponseEntity<List<DeploymentResponse>> getAll() {
        return ResponseEntity.ok(deploymentService.getAll());
    }

}
