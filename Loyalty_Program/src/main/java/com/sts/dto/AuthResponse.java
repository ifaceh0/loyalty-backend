package com.sts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private Long id;
    private String name;
    private String companyEmail;

    public AuthResponse(String token, String message, Long id, String name) {
        this.token = token;
        this.message = message;
        this.id = id;
        this.name = name;
    }
}
