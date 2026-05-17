package com.pedasco.apex.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedasco.apex.domain.entity.Client;
import com.pedasco.apex.domain.entity.CommandLog;
import com.pedasco.apex.domain.enums.CommandAction;
import com.pedasco.apex.domain.enums.CommandStatus;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.repository.CommandLogRepository;
import com.pedasco.apex.websocket.ClientSessionRegistry;
import com.pedasco.apex.websocket.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {
    private final ClientRepository clientRepository;
    private final CommandLogRepository commandLogRepository;
    private final ClientSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public void sendCommand (UUID clientId, CommandAction action , Object payload) throws JsonProcessingException {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!sessionRegistry.isOnline(clientId.toString())) {
            throw new RuntimeException("Client is offline");
        }


        CommandLog commandLog  = new CommandLog();
        commandLog .setClient(client);
        commandLog .setAction(action);
        commandLog .setPayload(payload != null ? objectMapper.writeValueAsString(payload) : null);
        commandLog .setStatus(CommandStatus.PENDING);
        commandLogRepository.save(commandLog );


        WebSocketMessage message = new WebSocketMessage();

        message.setType("COMMAND");
        message.setAction(action.name());
        message.setPayload(payload);
        message.setMessageId(commandLog .getId().toString());
        message.setTimestamp(LocalDateTime.now().toString());


        WebSocketSession session = sessionRegistry.getSession(clientId.toString())
                .orElseThrow(() -> new RuntimeException("Session not Found"));

        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("Failed to send command to client: {}", client.getName(), e);
            throw new RuntimeException("Failed to send command", e);
        }


        log.info("Command sent: {} to client: {}", action, client.getName());

    }
}
