package com.pedasco.apex.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvisionResponse {

    private String clientId;
    private String token;
    private String name ;
    private String location;
}
