package com.pedasco.apex.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String type;
    private String action;
    private Object payload;
    private String messageId;
    private String timestamp;
}


