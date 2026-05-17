package com.pedasco.apex.api;


import com.pedasco.apex.dto.request.ProvisionRequest;
import com.pedasco.apex.dto.response.ProvisionResponse;
import com.pedasco.apex.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/provision")
@RequiredArgsConstructor
public class ProvisioningController {

    private final ProvisioningService provisioningService;

    @PostMapping
    public ResponseEntity<ProvisionResponse> provision(@RequestBody ProvisionRequest request){
        ProvisionResponse response = provisioningService.provision(request);
        return ResponseEntity.ok(response);
    }

}
