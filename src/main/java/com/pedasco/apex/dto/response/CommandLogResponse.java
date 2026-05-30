package com.pedasco.apex.dto.response;

import com.pedasco.apex.domain.enums.CommandAction;
import com.pedasco.apex.domain.enums.CommandStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommandLogResponse {
    private UUID id;
    private String payload;
    private CommandAction action;
    private CommandStatus status ;
    private LocalDateTime sentAt;
    private LocalDateTime resultAt;
}
