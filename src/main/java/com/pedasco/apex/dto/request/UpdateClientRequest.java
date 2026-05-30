package com.pedasco.apex.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientRequest {
    private String name;
    private String location;
}
