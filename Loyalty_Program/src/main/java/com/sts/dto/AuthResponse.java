package com.sts.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String message;
    private Long id;
    private String name;

    public AuthResponse(String token, String message, Long id, String name) {
        this.token = token;
        this.message = message;
        this.id = id;
        this.name = name;
    }
}
