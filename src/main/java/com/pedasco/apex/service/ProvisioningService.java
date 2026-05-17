package com.pedasco.apex.service;


import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.InstallKey;
import com.pedasco.apex.dto.request.ProvisionRequest;
import com.pedasco.apex.dto.response.ProvisionResponse;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.repository.InstallKeyRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProvisioningService {
    private final InstallKeyRepository installKeyRepository;
    private final ClientRepository clientRepository;


    public ProvisionResponse provision(ProvisionRequest request){
        InstallKey installKey =  installKeyRepository
                .findByKeyAndUsedFalse(request.getInstallKey())
                .orElseThrow(() -> new RuntimeException("Invalid install key"));


        if (installKey.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Install Key expired");
        }


        String token = UUID.randomUUID().toString();

        Client client = installKey.getClient();
        client.setToken(token);
        clientRepository.save(client);

        installKey.setUsed(true);
        installKey.setUsedAt(LocalDateTime.now());
        installKeyRepository.save(installKey);

        ProvisionResponse response = new ProvisionResponse();
        response.setClientId(client.getId().toString());
        response.setToken(token);
        response.setName(client.getName());
        response.setLocation(client.getLocation());
        return response;
    }

}
