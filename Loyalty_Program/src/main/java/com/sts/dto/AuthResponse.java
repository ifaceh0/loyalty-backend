package com.sts.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String message;
    private Long id;
    private String name;
    private SubscriptionDetails subscriptionDetails;

    public AuthResponse(String token, String message, Long id, String name) {
        this(token, message, id, name, null);
    }

    public AuthResponse(String token, String message, Long id, String name, SubscriptionDetails subscriptionDetails) {
        this.token = token;
        this.message = message;
        this.id = id;
        this.name = name;
        this.subscriptionDetails = subscriptionDetails;
    }
}
