package com.pedasco.apex.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String username;
    private String password;
    private String role; // "ADMIN", "OPERATOR", "VIEWER"
}
