package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.enums.ClientStatus;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.websocket.ClientSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientHeartbeatMonitor {

    private final ClientRepository clientRepository;

    @Scheduled(fixedDelay = 60000)
    public void checkDeadClients(){
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(60);
        List <Client> clients =  clientRepository.findByStatusAndLastSeenBefore(ClientStatus.ONLINE ,threshold);

        for (Client cl: clients){
            cl.setStatus(ClientStatus.OFFLINE);
        }
        clientRepository.saveAll(clients);

    }
}
