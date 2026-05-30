package com.pedasco.apex.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pedasco.apex.domain.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity

@Table(name = "clients")
public class Client extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String location;

    @Column(name = "client_version", length = 20)
    private String clientVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ClientStatus status = ClientStatus.OFFLINE;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(length = 500)
    private String token;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientConfig> configs;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<CommandLog> commandLogs;
}