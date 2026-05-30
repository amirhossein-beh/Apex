package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.ClientConfig;
import com.pedasco.apex.domain.entity.InstallKey;
import com.pedasco.apex.dto.request.UpdateClientRequest;
import com.pedasco.apex.dto.response.ClientDetailsResponse;
import com.pedasco.apex.dto.response.ClientResponse;
import com.pedasco.apex.dto.response.CommandLogResponse;
import com.pedasco.apex.dto.response.InstallKeyResponse;
import com.pedasco.apex.repository.ClientConfigRepository;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.repository.InstallKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientConfigRepository clientConfigRepository;
    private final InstallKeyRepository installKeyRepository;



    public List<ClientResponse> getAllClient(){
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .map((client)->{
                    ClientResponse clientResponse = new ClientResponse();

                    clientResponse.setId(client.getId());
                    clientResponse.setName(client.getName());
                    clientResponse.setLocation(client.getLocation());
                    clientResponse.setClientVersion(client.getClientVersion());
                    clientResponse.setStatus(client.getStatus());
                    clientResponse.setLastSeen(client.getLastSeen());
                    clientResponse.setToken(client.getToken());

                    return clientResponse;

                }).toList();

    }


    public ClientDetailsResponse getById(UUID client_id){
        Client client = clientRepository.findById(client_id)
                .orElseThrow(RuntimeException::new);

        List<CommandLogResponse> commandLogs =
                client.getCommandLogs()
                        .stream()
                        .map(log -> {

                            CommandLogResponse response =
                                    new CommandLogResponse();

                            response.setId(log.getId());
                            response.setPayload(log.getPayload());
                            response.setAction(log.getAction());
                            response.setStatus(log.getStatus());
                            response.setSentAt(log.getSentAt());
                            response.setResultAt(log.getResultAt());

                            return response;

                        })
                        .toList();

        ClientDetailsResponse clientDetails = new ClientDetailsResponse();
        clientDetails.setId(client.getId());
        clientDetails.setName(client.getName());
        clientDetails.setLocation(client.getLocation());
        clientDetails.setClientVersion(client.getClientVersion());
        clientDetails.setStatus(client.getStatus());
        clientDetails.setLastSeen(client.getLastSeen());
        clientDetails.setToken(client.getToken());
        clientDetails.setCommandLog(commandLogs);

        return clientDetails;

    }


    public String deleteById(UUID client_Id){

        clientRepository.deleteById(client_Id);

        return "client deleted";
    }


    public Client update(UUID clientId, UpdateClientRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (request.getName() != null)
            client.setName(request.getName());
        if (request.getLocation() != null)
            client.setLocation(request.getLocation());
        return clientRepository.save(client);
    }



    public List<ClientConfig> getConfig(UUID client_id){
        return clientConfigRepository.findByClientId(client_id);
    }

    public ClientConfig updateConfig(UUID clientId, String configKey, String configValue) {
        // اگه این key قبلاً بود آپدیت کن، اگه نبود بساز
        ClientConfig config = clientConfigRepository
                .findByClientIdAndConfigKey(clientId, configKey)
                .orElse(new ClientConfig());

        if (config.getClient() == null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            config.setClient(client);
            config.setConfigKey(configKey);
        }

        config.setConfigValue(configValue);
        return clientConfigRepository.save(config);
    }


    public InstallKeyResponse generateInstallKey(UUID clientId, String createdBy) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        //TODO  if we need to more data replace clientResponse with ClientDetailsResponse

        InstallKey installKey = new InstallKey();
        installKey.setKey("LPR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        installKey.setClient(client);
        installKey.setCreatedBy(createdBy);
        installKey.setExpiresAt(LocalDateTime.now().plusDays(7));
        InstallKey savedInstallKey = installKeyRepository.save(installKey);


        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(client.getId());
        clientResponse.setName(client.getName());
        clientResponse.setStatus(client.getStatus());
        clientResponse.setLastSeen(client.getLastSeen());
        clientResponse.setClientVersion(client.getClientVersion());

        InstallKeyResponse response = new InstallKeyResponse();
        response.setId(savedInstallKey.getId());
        response.setCreatedAt(savedInstallKey.getCreatedAt());
        response.setKey(savedInstallKey.getKey());
        response.setClient(clientResponse);
        response.setUsed(savedInstallKey.isUsed());
        response.setUsedAt(savedInstallKey.getUsedAt());
        response.setCreatedBy(savedInstallKey.getCreatedBy());
        response.setExpiresAt(savedInstallKey.getExpiresAt());


        return response;
    }
}
