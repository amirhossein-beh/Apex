package com.pedasco.apex.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "traffic_logs")
public class TrafficLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "plate_text", length = 50)
    private String plateText;

    @Column
    private Double confidence;

    @Column(length = 50)
    private String country;

    @Column(length = 5)
    private String direction;

    @Column(name = "stream_id")
    private Integer streamId;

    @Column(name = "plate_image_path", length = 500)
    private String plateImagePath;

    @Column(name = "car_image_path", length = 500)
    private String carImagePath;

    @Column(name = "log_date", length = 10)
    private String logDate;

    @Column(name = "log_time", length = 10)
    private String logTime;

    @CreationTimestamp
    @Column(name = "received_at", updatable = false)
    private LocalDateTime receivedAt;

    @Column(name = "forwarded_to_ghadir", nullable = false)
    private boolean forwardedToGhadir = false;

    @Column(name = "forwarded_at")
    private LocalDateTime forwardedAt;
}