package com.pedasco.apex.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficLogRequest {
    private String plateText;
    private Double confidence;
    private String country;
    private String direction;
    private Integer streamId;
    private String plateImageBase64;
    private String carImageBase64;
    private String logDate;
    private String logTime;
}