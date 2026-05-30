package com.pedasco.apex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TrafficLogResponse {
    private UUID id;
    private UUID clientId;
    private String plateText;
    private Double confidence;
    private String country;
    private String direction;
    private Integer streamId;
    private String plateImagePath;
    private String carImagePath;
    private String logDate;
    private String logTime;
    private LocalDateTime receivedAt;
    private String refID;
}
