package com.pedasco.apex.dto.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommandRequest {
    private String action;
    private Object payload;
}
