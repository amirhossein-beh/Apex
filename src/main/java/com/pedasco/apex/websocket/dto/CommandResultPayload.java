package com.pedasco.apex.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandResultPayload {
    private boolean success;
    private String message;
}