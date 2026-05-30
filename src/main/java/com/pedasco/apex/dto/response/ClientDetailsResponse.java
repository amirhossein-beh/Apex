package com.pedasco.apex.dto.response;

import com.pedasco.apex.domain.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClientDetailsResponse {
    private UUID id;
    private String createdAt;
    private String name;
    private String location;
    private String clientVersion ;
    private ClientStatus status;
    private LocalDateTime lastSeen;
    private String token;
    private List<CommandLogResponse> commandLog;
}
