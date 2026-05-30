package com.pedasco.apex.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedasco.apex.domain.entity.CommandLog;
import com.pedasco.apex.domain.enums.ClientStatus;
import com.pedasco.apex.domain.enums.CommandStatus;
import com.pedasco.apex.repository.ClientRepository;
import com.pedasco.apex.repository.CommandLogRepository;
import com.pedasco.apex.service.TrafficService;
import com.pedasco.apex.websocket.dto.CommandResultPayload;
import com.pedasco.apex.websocket.dto.TrafficLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import com.pedasco.apex.websocket.dto.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApexWebSocketHandler extends TextWebSocketHandler {

    private final ClientSessionRegistry sessionRegistry;
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandLogRepository commandLogRepository;
    private final TrafficService trafficService;
    // وقتی کلاینت وصل میشه
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // token رو از query string میگیره: ws://server/ws?token=xxx
        String token = getTokenFromSession(session);

        if (token == null) {
            // token نداره، قطع کن
            closeSession(session);
            return;
        }

        // با token کلاینت رو پیدا کن
        clientRepository.findByToken(token).ifPresentOrElse(client -> {
            // session رو ثبت کن
            sessionRegistry.register(client.getId().toString(), session);

            // وضعیت کلاینت رو ONLINE کن
            client.setStatus(ClientStatus.ONLINE);
            client.setLastSeen(LocalDateTime.now());
            clientRepository.save(client);

            log.info("Client connected: {}", client.getName());
        }, () -> {
            // token نامعتبره، قطع کن
            log.warn("Unknown token, closing session: {}", token);
            closeSession(session);
        });
    }

    // وقتی پیام میاد از کلاینت
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try{
            WebSocketMessage incoming = objectMapper.readValue(
                    message.getPayload(),
                    WebSocketMessage.class
            );
            switch (incoming.getType()) {
                case "HEARTBEAT" ->      handleHeartbeat(session);
                case "COMMAND_RESULT" -> handleCommandResult(incoming);
                case "TRAFFIC_LOG" ->    handleTrafficLog(session, incoming);
            }
        }catch (Exception e){
            log.error("Error processing message" , e);
        }
    }


    private void handleHeartbeat(WebSocketSession session){
        String token = getTokenFromSession(session);
        clientRepository.findByToken(token).ifPresent(client -> {
            client.setLastSeen(LocalDateTime.now());
            clientRepository.save(client);
            log.debug("Heartbeat from: {}", client.getName());
        });
    }


    private void handleCommandResult(WebSocketMessage incoming){
        var messageId = incoming.getMessageId();

        CommandLog commandLog =  commandLogRepository.findById(UUID.fromString(messageId)).orElseThrow(() -> new RuntimeException("CommandLog not found"));

        CommandResultPayload result = objectMapper.convertValue(
                incoming.getPayload(),
                CommandResultPayload.class
        );

        if (result.isSuccess()){
            commandLog.setStatus(CommandStatus.SUCCESS);
        }else {
            commandLog.setStatus(CommandStatus.FAILED);
        }
        commandLog.setResultAt(LocalDateTime.now());
        commandLogRepository.save(commandLog);
    }


    private void handleTrafficLog(WebSocketSession session , WebSocketMessage incoming){

        String token = getTokenFromSession(session);
        clientRepository.findByToken(token).ifPresent( client -> {
            try {
                TrafficLogRequest request = objectMapper.convertValue(
                        incoming.getPayload(),
                        TrafficLogRequest.class);
                trafficService.saveTrafficLog(client.getId(), request);
            }catch (Exception e ){
                log.error("Failed to save traffic log from WebSocket", e);
            }
        });
    }

    // وقتی کلاینت قطع میشه
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = getTokenFromSession(session);

        if (token != null) {
            clientRepository.findByToken(token).ifPresent(client -> {
                // session رو حذف کن
                sessionRegistry.remove(client.getId().toString());

                // وضعیت رو OFFLINE کن
                client.setStatus(ClientStatus.OFFLINE);
                clientRepository.save(client);

                log.info("Client disconnected: {}", client.getName());
            });
        }
    }

    // token رو از URL میگیره
    private String getTokenFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery(); // token=xxx
        if (query != null && query.startsWith("token=")) {
            return query.substring(6);
        }
        return null;
    }

    // session رو میبنده
    private void closeSession(WebSocketSession session) {
        try {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error("Error closing session", e);
        }
    }
}

